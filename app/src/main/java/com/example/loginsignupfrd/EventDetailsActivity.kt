package com.example.loginsignupfrd

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventDetailsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventAdapter: EventsAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var searchView: SearchView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        drawerLayout = findViewById(R.id.main_drawer_layout)

        // Set navigation item listener
        val navigationView = findViewById<NavigationView>(R.id.main_nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventsAdapter()
        eventsRecyclerView.adapter = eventAdapter

        searchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // No action needed here
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                eventAdapter.filter.filter(newText)
                return false
            }
        })

        // Initialize Firebase reference
        databaseRef = FirebaseDatabase.getInstance().getReference("events")

        // Fetch all events
        fetchAllEvents()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item clicks here
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_about -> {
                val intent = Intent(this, AboutUsActivity::class.java)
                startActivity(intent)
                finish()            }
            R.id.nav_logout -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun fetchAllEvents() {
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val eventsList = mutableListOf<Event>()
                for (eventSnapshot in dataSnapshot.children) {
                    val id = eventSnapshot.key.toString()
                    val eventName = eventSnapshot.child("eventName").getValue(String::class.java) ?: ""
                    val age = eventSnapshot.child("age").getValue(Int::class.java) ?: 0
                    val gender = eventSnapshot.child("gender").getValue(String::class.java) ?: ""
                    val phoneNumber = eventSnapshot.child("phoneNumber").getValue(String::class.java) ?: ""
                    val sportsName = eventSnapshot.child("sportsName").getValue(String::class.java) ?: ""
                    val sportCategory = eventSnapshot.child("sportCategory").getValue(String::class.java) ?: ""
                    val date = eventSnapshot.child("date").getValue(String::class.java) ?: ""
                    val time = eventSnapshot.child("time").getValue(String::class.java) ?: ""
                    val location = eventSnapshot.child("location").getValue(String::class.java) ?: ""
                    val email = eventSnapshot.child("email").getValue(String::class.java) ?: ""

                    val event = Event(id, eventName, phoneNumber, sportsName, sportCategory, time, date, location, email, age, gender)
                    eventsList.add(event)
                }
                eventAdapter.submitList(eventsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("EventDetailsActivity", "Database error: ${error.message}")
            }
        })
    }
}
