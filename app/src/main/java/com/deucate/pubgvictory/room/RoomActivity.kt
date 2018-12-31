package com.deucate.pubgvictory.room

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.utils.Constatns
import com.deucate.pubgvictory.utils.Util
import kotlinx.android.synthetic.main.activity_room.*
import org.json.JSONException
import org.json.JSONObject
import android.content.IntentFilter
import instamojo.library.InstapayListener
import instamojo.library.InstamojoPay

class RoomActivity : AppCompatActivity() {

    private lateinit var util: Util

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        util = Util(this)
        val room = intent.getSerializableExtra(Constatns.rooms) as Room
        initViews(room)

        roomParticipateButton.setOnClickListener {
            callInstamojoPay("patelkartik1910@gmail.com","6352122123","200","test","kartik patel")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(room: Room) {
        if (!room.AuthorImage.isEmpty()) {
            Glide.with(this).load(room.AuthorImage).into(roomAuthorImage)
        }
        if (!room.Image.isEmpty()) {
            Glide.with(this).load(room.Image).into(roomImage)
        }

        roomTitle.text = room.Title
        roomDescription.text = room.GameDescription
        roomEntryFees.text = "₹${room.EntryFees}"
        roomPrice.text = "₹${room.Price}"
        roomTeam.text = util.getTypeOfTeam(room.Teams)
        roomAuthorName.text = "By ${room.AuthorName} at "
    }

    private fun callInstamojoPay(email: String, phone: String, amount: String, purpose: String, buyername: String) {
        val activity = this
        val instamojoPay = InstamojoPay()
        val filter = IntentFilter("ai.devsupport.instamojo")
        registerReceiver(instamojoPay, filter)
        val pay = JSONObject()
        try {
            pay.put("email", email)
            pay.put("phone", phone)
            pay.put("purpose", purpose)
            pay.put("amount", amount)
            pay.put("name", buyername)
            pay.put("send_sms", false)
            pay.put("send_email", false)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        instamojoPay.start(activity, pay, object : InstapayListener {
            override fun onSuccess(p0: String?) {
                util.showAlertDialog("Error",p0!!)
            }

            override fun onFailure(p0: Int, p1: String?) {
                util.showAlertDialog("Error",p1!!)
            }
        })
    }

}
