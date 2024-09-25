package com.example.hackapp.repository

import com.example.hackapp.api.RetrofitClient
import com.example.hackapp.model.Event
import com.example.hackapp.model.EventResponse
import android.util.Log
import com.google.gson.GsonBuilder

//model layer, ViewModel doesn't have to deal with backend, separation of concerns
//bridge between API and rest of app, retrieves API info as list of events
class EventRepository {
    private val api = RetrofitClient.api
    private val gson = GsonBuilder().setPrettyPrinting().create() //Gson instance to pretty-print JSON

    suspend fun getEvents(): List<Event> {
        try {
            //FETCH EVENTS
            val response: EventResponse = api.getEvents() //network request to fetch events of type EventResponse

            // Log the entire EventResponse
            Log.d("EventRepository", "EventResponse: ${gson.toJson(response)}")

            val events = response.events
            Log.d("EventRepository", "Number of events: ${events.size}")

            events.forEach { event -> //logging
                Log.d("EventRepository", "Event: ${event.name}")
                Log.d("EventRepository", "Event details: ${gson.toJson(event)}")
                Log.d("EventRepository", "Number of locations: ${event.locations.size}")
                event.locations.forEach { location ->
                    Log.d("EventRepository", "Location for ${event.name}: ${gson.toJson(location)}")
                    Log.d("EventRepository", "Latitude: ${location.latitude}, Longitude: ${location.longitude}")
                }
            }

            return events
        } catch (e: Exception) {
            Log.e("EventRepository", "Error fetching events", e)
            throw e
        }
    }
}