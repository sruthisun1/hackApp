package com.example.hackapp.model

import com.google.gson.annotations.SerializedName

//structures API info as list of events, part of Model layer
//Model layer, defines structure of data returned by API
data class EventResponse(
    @SerializedName("events") //Gson library, JSON field events mapped to events property
    val events: List<Event> //list of Event objects
)

