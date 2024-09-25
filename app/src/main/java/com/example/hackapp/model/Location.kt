package com.example.hackapp.model

//model layer, used for maps portion
data class Location(
    val description: String,
    val tags: List<String>,
    val latitude: Double,
    val longitude: Double
)