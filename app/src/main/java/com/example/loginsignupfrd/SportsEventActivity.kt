package com.example.loginsignupfrd

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class SportsEventActivity : AppCompatActivity() {

    private lateinit var eventNameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var sportsNameEditText: EditText
    private lateinit var sportCategorySpinner: Spinner
    private lateinit var locationEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var selectedGender: String
    private lateinit var calendar: Calendar

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sportsevent)

        // Initialize EditTexts
        eventNameEditText = findViewById(R.id.eventNameEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        sportsNameEditText = findViewById(R.id.sportsNameEditText)
        sportCategorySpinner = findViewById(R.id.sportCategorySpinner)
        locationEditText = findViewById(R.id.locationEditText)
        emailEditText = findViewById(R.id.emailEditText)
        ageEditText = findViewById(R.id.ageEditText)
        genderRadioGroup = findViewById(R.id.genderRadioGroup)
        selectedGender = "Male" // Default to Male

        calendar = Calendar.getInstance()

        // Set up spinner
        val sportsCategories = arrayOf("Football", "Basketball", "Tennis", "Cricket", "Running", "Cycling", "Others")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, sportsCategories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sportCategorySpinner.adapter = adapter

        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            if (validateInput()) {
                saveEventToFirebase()
            }
        }

        // Set onClickListener for date picker
        findViewById<Button>(R.id.datePickerButton).setOnClickListener {
            showDatePicker()
        }

        // Set onClickListener for time picker
        findViewById<Button>(R.id.timePickerButton).setOnClickListener {
            showTimePicker()
        }

        // Set OnCheckedChangeListener for genderRadioGroup
        genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            selectedGender = when (checkedId) {
                R.id.maleRadioButton -> "Male"
                R.id.femaleRadioButton -> "Female"
                else -> "Other"
            }
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

    private fun saveEventToFirebase() {

        val eventName = eventNameEditText.text.toString()
        val phoneNumber = phoneNumberEditText.text.toString()
        val sportsName = sportsNameEditText.text.toString()
        val sportCategory = sportCategorySpinner.selectedItem.toString()
        val time = formatTime(calendar.time)
        val date = formatDate(calendar.time)
        val location = locationEditText.text.toString()
        val email = emailEditText.text.toString()
        val age = ageEditText.text.toString().toIntOrNull() ?: 0 // Convert to Int or default to 0 if invalid
        val gender = selectedGender

        val database = FirebaseDatabase.getInstance()
        val eventsRef = database.getReference("events")

        val id = eventsRef.push().key.toString()

        val event = Event(id,eventName, phoneNumber, sportsName, sportCategory, time, date, location, email, age, gender)
        val eventRef = eventsRef.child(id)
        eventRef.setValue(event)

        // Show a dialog with the event details
        val eventDetails = "Name: $eventName\n" +
                "Phone Number: $phoneNumber\n" +
                "Email: $email\n" +
                "Event Name: $sportsName\n" +
                "Sport Category: $sportCategory\n" +
                "Date: $date\n" +
                "Time: $time\n" +
                "Location: $location\n" +
                "Age: $age\n" +
                "Gender: $gender"

        AlertDialog.Builder(this)
            .setTitle("Event Details")
            .setMessage(eventDetails)
            .setPositiveButton("OK") { _, _ ->
                // Clear EditText fields after saving
                eventNameEditText.text.clear()
                phoneNumberEditText.text.clear()
                sportsNameEditText.text.clear()
                locationEditText.text.clear()
                emailEditText.text.clear()
                ageEditText.text.clear()

                // Reset gender selection
                genderRadioGroup.clearCheck()

                sportCategorySpinner.setSelection(0)

                // Show success message
                Toast.makeText(this, "Event saved successfully", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun validateInput(): Boolean {
        // Add your validation logic here
        // For example, you can check if the required fields are not empty
        if (eventNameEditText.text.isBlank() ||
            phoneNumberEditText.text.isBlank() ||
            sportsNameEditText.text.isBlank() ||
            locationEditText.text.isBlank() ||
            ageEditText.text.isBlank() ||
            !isValidEmail(emailEditText.text.toString())
        ) {
            // Show error message or toast indicating required fields
            // You can customize this part as per your UI design
            // For example, you can show a toast message:
            Toast.makeText(this, "Please fill in all the fields correctly", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    data class Event(
        val id:String,
        val eventName: String,
        val phoneNumber: String,
        val sportsName: String,
        val sportCategory: String,
        val time: String,
        val date: String,
        val location: String,
        val email: String,
        val age: Int,
        val gender: String
    )
}