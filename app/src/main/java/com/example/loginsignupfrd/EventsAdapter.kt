package com.example.loginsignupfrd

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>(), Filterable {

    private var eventsList = mutableListOf<Event>()
    private var filteredEventsList = mutableListOf<Event>()

    init {
        filteredEventsList = eventsList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = filteredEventsList[position]
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return filteredEventsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Event>) {
        eventsList = list.toMutableList()
        filteredEventsList = eventsList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val query = charSequence.toString().lowercase(Locale.getDefault())
                val filterResults = FilterResults()
                filterResults.values = if (query.isEmpty()) {
                    eventsList
                } else {
                    eventsList.filter {
                        it.eventName.lowercase(Locale.getDefault()).contains(query) ||
                                it.sportsName.lowercase(Locale.getDefault()).contains(query) ||
                                it.location.lowercase(Locale.getDefault()).contains(query) ||
                                it.sportCategory.lowercase(Locale.getDefault()).contains(query)
                    }.toMutableList()
                }
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filteredEventsList = filterResults?.values as MutableList<Event>
                notifyDataSetChanged()
            }
        }
    }

    @Suppress("DEPRECATION", "SameParameterValue")
    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventNameTextView: TextView = itemView.findViewById(R.id.eventNameTextView)
        private val ageTextView: TextView = itemView.findViewById(R.id.ageEditText)
        private val genderTextView: TextView = itemView.findViewById(R.id.genderRadioGroup)
        private val phoneNumberTextView: TextView = itemView.findViewById(R.id.phoneNumberEditText)
        private val sportsNameTextView: TextView = itemView.findViewById(R.id.sportsNameEditText)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val sportsCategorySpinner: TextView = itemView.findViewById(R.id.sportCategorySpinner)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val emailEditText: TextView = itemView.findViewById(R.id.emailEditText)
        private val callButton: Button = itemView.findViewById(R.id.callButton)
        private val chatButton: Button = itemView.findViewById(R.id.chatButton)

        init {
            callButton.setOnClickListener {
                val phoneNumber = filteredEventsList[adapterPosition].phoneNumber
                initiateCall(phoneNumber)
            }

            chatButton.setOnClickListener {
                val phoneNumber = filteredEventsList[adapterPosition].phoneNumber
                initiateChat(phoneNumber)
            }
        }

        private fun initiateCall(phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            itemView.context.startActivity(intent)
        }

        private fun initiateChat(phoneNumber: String) {
            // Format the phone number for WhatsApp
            val formattedPhoneNumber = phoneNumber.replace("\\s".toRegex(), "")

            // Create the intent
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$formattedPhoneNumber")

            // Check if WhatsApp is installed
            if (isAppInstalled("com.whatsapp")) {
                // Open WhatsApp chat
                intent.`package` = "com.whatsapp"
            } else {
                // Open the default SMS app if WhatsApp is not installed
                intent.data = Uri.parse("sms:$formattedPhoneNumber")
            }

            // Start the activity
            itemView.context.startActivity(intent)
        }

        private fun isAppInstalled(packageName: String): Boolean {
            return try {
                itemView.context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        fun bind(event: Event) {
            eventNameTextView.text = event.eventName
            ageTextView.text = event.age.toString()
            genderTextView.text = event.gender
            phoneNumberTextView.text = event.phoneNumber
            sportsNameTextView.text = event.sportsName
            sportsCategorySpinner.text = event.sportCategory
            dateTextView.text = event.date
            timeTextView.text = event.time
            locationTextView.text = event.location
            emailEditText.text = event.email
        }
    }
}
