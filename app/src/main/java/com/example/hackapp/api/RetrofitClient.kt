package com.example.hackapp.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//fetch data from API and use Gson to convert to kotlin objects
object RetrofitClient {
    private const val BASE_URL = "https://adonix.hackillinois.org/event/"

    //logs request and response data for debugging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //OkHttpClient instance with logging feature when fetching data from API
    private val okHttpClient = OkHttpClient.Builder() //helps app make network requests
        .addInterceptor(loggingInterceptor)
        .build()

    //retrofit instance converts JSON data into Kotlin objects to work with, like EventResponse
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        //Gson is a library that helps convert JSON to Kotlin objects for use
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //generates implementation of HackIllinois interface and can now call getEvents()/HackIllinoisApi methods
    val api: HackIllinoisApi = retrofit.create(HackIllinoisApi::class.java)
}
