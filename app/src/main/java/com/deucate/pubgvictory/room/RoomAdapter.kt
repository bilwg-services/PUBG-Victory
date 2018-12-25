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
        fun onClickShare(room: Room)
    }

    lateinit var listener: RoomCardClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.room_card,parent,false))
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

        holder.shareBTN.setOnClickListener {
            listener.onClickShare(room)
        }

    }

    @SuppressLint("SimpleDateFormat")
    private fun getFormattedDate(smsTimeInMilis: Long): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = smsTimeInMilis

        val now = Calendar.getInstance()

        return if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
            "Today"
        } else if (now.get(Calendar.DATE) + smsTime.get(Calendar.DATE) == 1) {
            "Tomorrow"
        } else {
            SimpleDateFormat("dd/MM/yyyy").format(Date(smsTimeInMilis))
        }

    }

}

class RoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val titleTV = view.roomTitle!!
    val timeTV = view.roomTime!!
    val authorImage = view.roomAuthorImage!!
    val gameImage = view.roomImage!!
    val gameDescriptionTV = view.roomDescription!!
    val priceTV = view.roomPrice!!
    val shareBTN = view.roomShare!!
    val mainCard = view.roomCardView!!
}