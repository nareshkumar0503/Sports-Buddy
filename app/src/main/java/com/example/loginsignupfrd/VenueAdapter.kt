package com.example.loginsignupfrd

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class VenueAdapter(private val venueList: List<Venue>) : RecyclerView.Adapter<VenueAdapter.VenueViewHolder>() {

    class VenueViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val venueImage: ImageView = view.findViewById(R.id.venueImage)
        val venueName: TextView = view.findViewById(R.id.venueName)
        val venueDescription: TextView = view.findViewById(R.id.venueDescription)
        val venueLocation: TextView = view.findViewById(R.id.venueLocation)
        val viewDetailsButton: Button = view.findViewById(R.id.viewDetailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venue, parent, false)
        return VenueViewHolder(view)
    }

    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val venue = venueList[position]
        holder.venueName.text = venue.name
        holder.venueDescription.text = venue.description
        holder.venueLocation.text = venue.location
        Glide.with(holder.itemView.context).load(venue.imageUrl).into(holder.venueImage)

        holder.viewDetailsButton.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, VenueDetailsActivity::class.java).apply {
                putExtra("venueName", venue.name)
                putExtra("venueDescription", venue.description)
                putExtra("venueLocation", venue.location)
                putExtra("venueImageUrl", venue.imageUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = venueList.size
}
