package com.example.hackapp.ui.slideshow

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.hackapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Event(
    val name: String = "",
    val description: String = "",
    val startTime: Long = 0,
    val endTime: Long = 0,
    val eventType: String = "",
    val locations: List<Location> = emptyList()
)

data class Location(
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

class SlideshowFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val bookmarkedEventsList = mutableListOf<Event>() // List to store Event objects

    override fun onCreateView( //inflates layout and initializes UI components
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        listView = root.findViewById(R.id.listViewBookmarkedEvents)

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Get current user ID
        val userId = auth.currentUser?.uid
        Log.d("SlideshowFragment", "Current User ID: $userId")

        // Fetch bookmarked events
        if (userId != null) {
            db.collection("users")
                .document(userId)
                .collection("bookmarkedEvents") //corrected spelling here to match Firestore
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        Log.d("Firestore", "No bookmarked events found.")
                    } else {
                        for (document in documents) {
                            val event = document.toObject(Event::class.java)//convert a Firestore document into an instance of the Event data class
                            bookmarkedEventsList.add(event)//add to bookmarkedEventsList, list of objects
                            Log.d("Firestore", "Found event: ${event.name}")
                        }

                        // Populate ListView with event names
                        val eventNames = bookmarkedEventsList.map { it.name }
                        val adapter = ArrayAdapter( //adding to list
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            eventNames
                        )
                        listView.adapter = adapter
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore Error", "Error getting documents: ", exception)
                }
        } else {
            Log.d("SlideshowFragment", "User is not logged in.")
        }

        return root
    }
}
