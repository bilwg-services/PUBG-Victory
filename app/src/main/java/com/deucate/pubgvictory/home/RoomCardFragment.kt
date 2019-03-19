package com.deucate.pubgvictory.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Util
import kotlinx.android.synthetic.main.activity_room.view.*
import kotlinx.android.synthetic.main.fragment_card_room.view.*

class RoomCardFragment : Fragment() {

    private lateinit var room: Room

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_card_room, container, false)
        room = arguments?.getSerializable("room") as Room

        //loading image into views
        if (room.AuthorImage != null) {
            Glide.with(this).load(room.AuthorImage).into(rootView.roomCardAuthorImage)
        }
        if (!room.Image.isEmpty()) {
            Glide.with(this).load(room.Image).into(rootView.roomCardImage)
        }

        rootView.roomCardTitle.text = room.Title
        rootView.roomCardDescription.text = room.GameDescription
        rootView.roomCardPrice.text = "₹${room.Price}"
        rootView.roomCardAuthorName.text = Util().getFormattedDate(room.Time)
        rootView.roomCardEntryFees.text = "₹${room.EntryFees}"

        rootView.roomParticipateButton.setOnClickListener {
            Toast.makeText(context, room.Title, Toast.LENGTH_LONG).show()
        }

        return rootView
    }
}