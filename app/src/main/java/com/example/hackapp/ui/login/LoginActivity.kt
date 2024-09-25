package com.example.hackapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hackapp.R
import com.example.hackapp.databinding.LoginactivityBinding
import com.example.hackapp.MainActivity
import com.example.hackapp.ui.login.SignUp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginactivityBinding //to access UI elements
    private lateinit var auth: FirebaseAuth //instance of firebaseauth for authentication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //initializes view binding and Firebase authentication
        binding = LoginactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() //initialize instance

        binding.buttonSignIn.setOnClickListener { //gets user inputted data for signIn
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            signIn(email, password)
        }

        binding.buttonSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) { //if no input, return
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) { //if successful, starts mainactivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(baseContext, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
