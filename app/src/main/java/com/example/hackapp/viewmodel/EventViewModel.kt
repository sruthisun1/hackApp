package com.example.hackapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackapp.model.Event
import com.example.hackapp.repository.EventRepository
import kotlinx.coroutines.launch

class EventViewModel : ViewModel() {
    private val repository = EventRepository() //instance of repo

    private val _events = MutableLiveData<List<Event>>()//private list of events
    val events: LiveData<List<Event>> = _events //public LiveData object

    private val _isLoading = MutableLiveData<Boolean>() //tracks whether data in progress
    val isLoading: LiveData<Boolean> = _isLoading//error tracks any error messages

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadEvents() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _events.value = repository.getEvents() //getch event data and update events
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load events: ${e.message}"
                _events.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun loadEventsIfNeeded() {
        if (_events.value.isNullOrEmpty()) { //checks if events in empty to load events only when necessary
            loadEvents()
        }
    }

}