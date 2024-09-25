package com.example.hackapp

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore

class MyApp : Application() {
    lateinit var db: FirebaseFirestore // Declare the db variable

    override fun onCreate() {
        super.onCreate()
        //below needed to use services
        FirebaseApp.initializeApp(this) // Initialize Firebase
        db = FirebaseFirestore.getInstance() // Initialize Firestore instance
    }
}
