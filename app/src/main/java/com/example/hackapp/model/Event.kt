package com.example.hackapp.model

//Model layer, data structure to display event info
data class Event(
    val eventId: String,
    val name: String,
    val description: String,
    val startTime: Long,
    val endTime: Long,
    val locations: List<Location>,
    val sponsor: String,
    val eventType: String,
    val points: Int,
    val isAsync: Boolean,
    val mapImageUrl: String,
    val isPro: Boolean,
    var isBookmarked: Boolean = false
)