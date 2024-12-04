package com.example.loginsignupfrd

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MySports : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: MySportsAdapter
    private lateinit var databaseRef: DatabaseReference
    private lateinit var drawerLayout: DrawerLayout // Declare drawerLayout

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mysports)

        drawerLayout = findViewById(R.id.main_drawer_layout) // Initialize drawerLayout

        // Set navigation item listener
        val navigationView = findViewById<NavigationView>(R.id.main_nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsAdapter = MySportsAdapter() // Initialize adapter here
        eventsRecyclerView.adapter = eventsAdapter // Set the adapter to the RecyclerView

        // Initialize Firebase reference
        databaseRef = FirebaseDatabase.getInstance().getReference("events")

        // Retrieve user events data for the currently logged-in user
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val currentUserEmail = sharedPreferences.getString("email", "")

        if (currentUserEmail.isNullOrEmpty()) {
            Toast.makeText(this, "No email found", Toast.LENGTH_SHORT).show()
        } else {
            fetchEvents(currentUserEmail)
        }
    }

    private fun fetchEvents(email: String) {
        val userEventsRef = databaseRef.orderByChild("email").equalTo(email)
        userEventsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val eventsList = mutableListOf<Event>()
                for (eventSnapshot in dataSnapshot.children) {
                    val event = eventSnapshot.getValue(Event::class.java)
                    event?.let {
                        eventsList.add(it)
                    }
                }
                eventsAdapter.submitList(eventsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MySports", "Database error: ${error.message}")
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation item clicks here
        when (item.itemId) {
            // Handle different menu items as needed
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
}
