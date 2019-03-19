package com.deucate.pubgvictory.matches

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Event
import kotlinx.android.synthetic.main.card_my_events.view.*
import java.text.SimpleDateFormat

class MatchAdapter(
    private val events: ArrayList<Event>,
    private val listener: (position: Int, event: Event) -> Unit
) :
    RecyclerView.Adapter<MatchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder =
        MatchViewHolder(
            LayoutInflater.from(parent.context!!).inflate(
                R.layout.card_my_events,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = events.size

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val event = events[position]
        holder.priceTV.text = "₹ ${event.Price}"
        holder.titleTV.text = event.Title
        holder.timeTv.text = SimpleDateFormat("dd/MM/yyyy hh:mm aa").format(event.Time.toDate())

        holder.itemView.setOnClickListener {
            listener(position, event)
        }
    }
}


class MatchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleTV = view.cardEventTitle!!
    val timeTv = view.cardEventTime!!
    val priceTV = view.cardEventPrice!!
}