package com.example.hackapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hackapp.R
import com.example.hackapp.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale
//View, handle presentation of the data
//listens for changed in data of ViewModel represented as Event
//Solely responsible for displaying event data
//updates LiveData, events, with new list of events if nexessary
class EventAdapter : ListAdapter<Event, EventAdapter.EventViewHolder>(EventDiffCallback()) {
    //new instance of EvenViewHolder that inflates layout for individual list items
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    //binds event data to views in EventViewHolder, fetches event from list using position and populates the views
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }


    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //characteristics displayed in view
        private val textEventName: TextView = itemView.findViewById(R.id.textEventName)
        private val textDateTime: TextView = itemView.findViewById(R.id.textDateTime)
        private val textDescription: TextView = itemView.findViewById(R.id.textDescription)
        private val switchBookmark: Switch = itemView.findViewById(R.id.switch_bookmark)

        // Firestore instance and user ID
        private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""


        fun bind(event: Event) {
            textEventName.text = event.name
            textDateTime.text = formatDateTime(event.startTime, event.endTime)
            textDescription.text = event.description

            //Set initial state for the bookmark switch
            switchBookmark.isChecked = event.isBookmarked

            // Handle bookmark toggle
            switchBookmark.setOnCheckedChangeListener { _, isChecked ->
                event.isBookmarked = isChecked
                // Save the updated bookmark status to Firestore
                saveBookmarkToFirestore(event)
            }
        }

        private fun saveBookmarkToFirestore(event: Event) {
            //Reference to the user's document in Firestore
            val userDocRef = db.collection("users").document(userId)

            //Reference to the event in the user's bookmarkedEvents subcollection
            val eventRef = userDocRef.collection("bookmarkedEvents").document(event.eventId)

            if (event.isBookmarked) {
                // Add or update the event in Firestore when it's bookmarked
                eventRef.set(event)
                    .addOnSuccessListener {
                        // Successfully saved the event bookmark
                    }
                    .addOnFailureListener { e ->
                        // Handle error when saving the bookmark
                        e.printStackTrace()
                    }
            } else {
                //Remove the event from Firestore if it's unbookmarked
                eventRef.delete()
                    .addOnSuccessListener {
                        // Successfully removed the event bookmark
                    }
                    .addOnFailureListener { e ->
                        // Handle error when removing the bookmark
                        e.printStackTrace()
                    }
            }
        }

        private fun formatDateTime(startTime: Long, endTime: Long): String { //formats start and end times
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val start = dateFormat.format(startTime * 1000) // Convert seconds to milliseconds
            val end = dateFormat.format(endTime * 1000) // Convert seconds to milliseconds
            return "$start - $end" // Format as "YYYY-MM-DD HH:MM - YYYY-MM-DD HH:MM"
        }
    }

    class EventDiffCallback : DiffUtil.ItemCallback<Event>() { //updates performance by only
        // updating items that have changed rather than refreshing whole list
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.eventId == newItem.eventId
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
}
