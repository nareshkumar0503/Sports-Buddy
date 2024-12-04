package com.example.loginsignupfrd

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VenueActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var venueAdapter: VenueAdapter
    private lateinit var venueList: MutableList<Venue>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venue) // Ensure this matches your XML file name

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        venueList = mutableListOf()
        venueAdapter = VenueAdapter(venueList)
        recyclerView.adapter = venueAdapter

        fetchVenues()
    }

    private fun fetchVenues() {
        val database = FirebaseDatabase.getInstance()
        val venuesRef = database.getReference("venues")

        venuesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                venueList.clear()
                for (venueSnapshot in snapshot.children) {
                    val venue = venueSnapshot.getValue(Venue::class.java)
                    if (venue != null) {
                        venueList.add(venue)
                    }
                }
                venueAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}
