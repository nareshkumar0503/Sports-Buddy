package com.example.loginsignupfrd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar
import java.util.Date
import java.util.Locale

class VenueDetailsActivity : AppCompatActivity() {

    private lateinit var calendar: Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue_details)

        val venueNameTextView: TextView = findViewById(R.id.venueNameTextView)
        val venueDescriptionTextView: TextView = findViewById(R.id.venueDescriptionTextView)
        val venueLocationTextView: TextView = findViewById(R.id.venueLocationTextView)
        val venueImageView: ImageView = findViewById(R.id.venueImageView)
        val submitDetailsButton: Button = findViewById(R.id.submitDetailsButton)
        val numberOfPeopleEditText: EditText = findViewById(R.id.numberOfPeopleEditText)

        // Get data from intent
        val venueName = intent.getStringExtra("venueName")
        val venueDescription = intent.getStringExtra("venueDescription")
        val venueLocation = intent.getStringExtra("venueLocation")
        val venueImageUrl = intent.getStringExtra("venueImageUrl")

        calendar = Calendar.getInstance()

        // Set data to views
        venueName?.let { venueNameTextView.text = it }
        venueDescription?.let { venueDescriptionTextView.text = it }
        venueLocation?.let { venueLocationTextView.text = it }
        venueImageUrl?.let { Glide.with(this).load(it).into(venueImageView) }

        val dateOfPlayEditText=findViewById<Button>(R.id.datePickerButton)
        val timePickerButton=findViewById<Button>(R.id.timePickerButton)

        dateOfPlayEditText.setOnClickListener {
            showDatePicker()
        }

        timePickerButton.setOnClickListener {
            showTimePicker()
        }

        submitDetailsButton.setOnClickListener {
            // Handle submit button click
            val timeOfPlay = timePickerButton.text.toString()
            val dateOfPlay = dateOfPlayEditText.text.toString()
            val numberOfPeople = numberOfPeopleEditText.text.toString().toIntOrNull() ?: 0

            // Create a VenueDetails object
            val venueDetails = VenueDetails(
                venueName,
                venueDescription,
                venueLocation,
                venueImageUrl,
                dateOfPlay,
                timeOfPlay,
                numberOfPeople
            )

            // Store the details in Firebase
            storeDetailsInFirebase(venueDetails)
        }
    }

    private fun showDatePicker() {
        val date = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                findViewById<Button>(R.id.datePickerButton).text = formatDate(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        date.datePicker.minDate = System.currentTimeMillis() - 1000
        date.show()
    }

    private fun showTimePicker() {
        val time = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                findViewById<Button>(R.id.timePickerButton).text = formatTime(calendar.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false
        )
        time.show()
    }

    private fun formatDate(date: Date): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(date)
    }

    private fun formatTime(date: Date): String {
        val sdf = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    private fun storeDetailsInFirebase(venueDetails: VenueDetails) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("venueDetails")

        ref.push().setValue(venueDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Details stored successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to store details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}

data class VenueDetails(
    val venueName: String?,
    val venueDescription: String?,
    val venueLocation: String?,
    val venueImageUrl: String?,
    val dateOfPlay: String,
    val timeOfPlay: String,
    val numberOfPeople: Int
)
