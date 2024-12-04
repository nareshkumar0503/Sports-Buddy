package com.example.loginsignupfrd

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class MySportsAdapter : RecyclerView.Adapter<MySportsAdapter.MySportsViewHolder>() {

    private var eventsList: List<Event> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySportsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mysports_event, parent, false)
        return MySportsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MySportsViewHolder, position: Int) {
        val currentEvent = eventsList[position]
        holder.bind(currentEvent)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    fun submitList(list: List<Event>) {
        eventsList = list
        notifyDataSetChanged()
    }

    inner class MySportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventNameTextView: TextView = itemView.findViewById(R.id.eventNameTextView)
        private val ageTextView: TextView = itemView.findViewById(R.id.ageEditText)
        private val genderTextView: TextView = itemView.findViewById(R.id.genderRadioGroup)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberEditText)
        private val sportsNameTextView: TextView = itemView.findViewById(R.id.sportsNameEditText)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val sportsCategoryTextView: TextView = itemView.findViewById(R.id.sportCategorySpinner)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailEditText)
        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        init {
            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = eventsList[position]
                    val intent = Intent(itemView.context, EditEventActivity::class.java)
                    intent.putExtra("event", event) // Put the event directly into the intent extras
                    itemView.context.startActivity(intent)
                }
            }


            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val event = eventsList[position]
                    deleteEvent(event)
                    eventsList = eventsList.toMutableList().apply {
                        removeAt(position)
                    }
                    notifyItemRemoved(position)
                }
            }

        }

        private fun deleteEvent(event: Event) {
            // Here you should delete the event from Firebase
            val database = FirebaseDatabase.getInstance()
            val eventsRef = database.getReference("events")
                eventsRef.child(event.id.toString()).removeValue()
                    .addOnSuccessListener {
                        Log.d("MySportsAdapter", "Event deleted successfully from Firebase")
                    }
                    .addOnFailureListener { e ->
                        Log.e("MySportsAdapter", "Error deleting event from Firebase: $e")
                    }

        }


        fun bind(event: Event) {
            eventNameTextView.text = event.eventName
            ageTextView.text = event.age.toString() // Convert age to String if it's not already
            genderTextView.text = event.gender
            phoneNumberTextView.text = event.phoneNumber
            sportsNameTextView.text = event.sportsName
            sportsCategoryTextView.text = event.sportCategory
            dateTextView.text = event.date
            timeTextView.text = event.time
            locationTextView.text = event.location
            emailTextView.text = event.email
        }
    }
}
