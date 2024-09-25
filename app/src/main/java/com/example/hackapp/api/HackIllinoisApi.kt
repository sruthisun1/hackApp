package com.example.hackapp.api

import com.example.hackapp.model.EventResponse
import retrofit2.http.GET
//Retrofit is a library to make network requests in Android
interface HackIllinoisApi {
     @GET(".") //GET request to base URL
     suspend fun getEvents(): EventResponse //suspend prevents UI freezes
}

