package com.example.loginsignupfrd

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class EditEventActivity : AppCompatActivity() {
    private lateinit var eventNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genderEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var sportsNameEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var sportsCategoryEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        // Retrieve event data from intent extras
        val event = intent.getSerializableExtra("event") as? Event
        val dataref=FirebaseDatabase.getInstance().reference.child("events").child(event?.id.toString())

        eventNameEditText = findViewById(R.id.eventNameEditText)
        ageEditText = findViewById(R.id.ageEditText)
        genderEditText = findViewById(R.id.genderEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        sportsNameEditText = findViewById(R.id.sportsNameEditText)
        dateEditText = findViewById(R.id.dateEditText)
        sportsCategoryEditText = findViewById(R.id.sportsCategoryEditText)
        timeEditText = findViewById(R.id.timeEditText)
        locationEditText = findViewById(R.id.locationEditText)
        emailEditText = findViewById(R.id.emailEditText)
        updateButton = findViewById(R.id.updateButton)

        // Populate UI fields with event data for editing
        event?.let {
            eventNameEditText.setText(it.eventName)
            ageEditText.setText(it.age.toString())
            genderEditText.setText(it.gender)
            phoneNumberEditText.setText(it.phoneNumber)
            sportsNameEditText.setText(it.sportsName)
            dateEditText.setText(it.date)
            sportsCategoryEditText.setText(it.sportCategory)
            timeEditText.setText(it.time)
            locationEditText.setText(it.location)
            emailEditText.setText(it.email)
        }

        updateButton.setOnClickListener {
            // Update event with new data
            event?.apply {
                eventName = eventNameEditText.text.toString()
                age = ageEditText.text.toString().toIntOrNull() ?: 0
                gender = genderEditText.text.toString()
                phoneNumber = phoneNumberEditText.text.toString()
                sportsName = sportsNameEditText.text.toString()
                date = dateEditText.text.toString()
                sportCategory = sportsCategoryEditText.text.toString()
                time = timeEditText.text.toString()
                location = locationEditText.text.toString()
                email = emailEditText.text.toString()
            }
            dataref.setValue(event)
                .addOnSuccessListener {
                    // Data updated successfully
                    // Pass updated event back to calling activity
                    Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show()

                    intent.putExtra("updatedEvent", event)
                    setResult(RESULT_OK, intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    // Error occurred while updating data
                    // Handle the error as needed
                    // For example, you can show a toast message
                    Toast.makeText(this, "Failed to update event: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
    }
}
