package com.example.loginsignupfrd

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonUpdateDetails: Button
    private lateinit var buttoncancel: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var databaseReference: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonUpdateDetails = findViewById(R.id.buttonUpdateDetails)
        buttoncancel = findViewById(R.id.buttoncancel)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")

        // Populate EditText fields with current user details from SharedPreferences
        editTextEmail.setText(sharedPreferences.getString("email", ""))
        editTextPassword.setText(sharedPreferences.getString("password", ""))

        buttoncancel.setOnClickListener {
            // Redirect to activity_user
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finish() // Finish the current activity
        }

        buttonUpdateDetails.setOnClickListener {
            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString() // Use email as unique identifier

            // Update data in Firebase Realtime Database
            val userQuery: Query = databaseReference.orderByChild("email").equalTo(email)

            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userId = userSnapshot.key // Get the unique ID
                            Log.d("ProfileActivity", "User found: $userId")
                            // Update user details in Firebase
                            userId?.let {
                                databaseReference.child(userId).child("password").setValue(password)
                            }

                            // Update user details in SharedPreferences
                            val editor = sharedPreferences.edit()
                            editor.putString("password", password)
                            editor.apply()

                            Toast.makeText(
                                this@ProfileActivity,
                                "Details updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            // Redirect back to DashboardFragment
                            startActivity(
                                Intent(
                                    this@ProfileActivity,
                                    MainActivity::class.java
                                ).apply {
                                    putExtra("fragmentToLoad", R.id.nav_profile)
                                })
                            finish()
                            return
                        }
                    } else {
                        Log.d("ProfileActivity", "User not found with email: $email")
                        Toast.makeText(this@ProfileActivity, "User not found", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("ProfileActivity", "Database error: ${databaseError.message}")
                    Toast.makeText(
                        this@ProfileActivity,
                        "Failed to update details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}
