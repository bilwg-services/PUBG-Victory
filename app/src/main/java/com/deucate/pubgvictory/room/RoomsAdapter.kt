package com.deucate.pubgvictory.room

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deucate.pubgvictory.R
import kotlinx.android.synthetic.main.room_card.view.*
import java.text.SimpleDateFormat
import java.util.*

class RoomAdapter(private val rooms: ArrayList<Room>) : RecyclerView.Adapter<RoomViewHolder>() {

    interface RoomCardClickListener {
        fun onClickCard(room: Room)
    }

    lateinit var listener: RoomCardClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_card, parent, false))
    }

    override fun getItemCount(): Int {
        return rooms.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]

        //loading image into views
        if (!room.AuthorImage.isEmpty()) {
            Glide.with(holder.itemView).load(room.AuthorImage).into(holder.authorImage)
        }
        if (!room.Image.isEmpty()) {
            Glide.with(holder.itemView).load(room.Image).into(holder.gameImage)
        }

        holder.titleTV.text = room.Title
        holder.gameDescriptionTV.text = room.GameDescription
        holder.priceTV.text = "₹${room.Price}"
        holder.timeTV.text = getFormattedDate(room.Time)

        holder.mainCard.setOnClickListener {
            listener.onClickCard(room)
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(smsTimeInMilis: Long): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = smsTimeInMilis

        val now = Calendar.getInstance()

        return when {
            now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) -> "Today"

            now.get(Calendar.DATE) + smsTime.get(Calendar.DATE) == 1 -> "Tomorrow"

            else -> SimpleDateFormat("dd/MM/yyyy").format(Date(smsTimeInMilis))
        }

    }

}

class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleTV = view.roomCardTitle!!
    val timeTV = view.roomCardAuthorName!!
    val authorImage = view.roomCardAuthorImage!!
    val gameImage = view.roomCardImage!!
    val gameDescriptionTV = view.roomCardDescription!!
    val priceTV = view.roomCardPrice!!
    val mainCard = view.roomCardView!!
}