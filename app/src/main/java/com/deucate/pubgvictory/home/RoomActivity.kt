package com.deucate.pubgvictory.home

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
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.view.LayoutInflater
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.model.User
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import instamojo.library.InstapayListener
import instamojo.library.InstamojoPay
import kotlinx.android.synthetic.main.sheet_get_detail.view.*

class RoomActivity : AppCompatActivity() {

    private lateinit var util: Util

    private val users = ArrayList<User>()
    private val auth = FirebaseAuth.getInstance()


    @SuppressLint("InflateParams", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        util = Util(this)
        val room = intent.getSerializableExtra(Constatns.rooms) as Room
        initViews(room)

        roomParticipateButton.setOnClickListener {
            // callInstamojoPay("patelkartik1910@gmail.com","6352122123","200","test","kartik patel")

            val view = LayoutInflater.from(this).inflate(R.layout.sheet_get_detail, null, false)
            val bottomDialog = BottomSheetDialog(this)

            view.nextButton.setOnClickListener {
                val name = view.detailNameET
                val id = view.pubgIDET

                if (TextUtils.isEmpty(name.text.toString())) {
                    name.error = "Please enter name."
                }
                if (TextUtils.isEmpty(id.text.toString())) {
                    id.error = "Please enter id."
                }

                users.add(
                    User(
                        ID = id.text.toString(),
                        Name = name.text.toString(),
                        Phone = null,
                        Email = null
                    )
                )

                name.text = SpannableStringBuilder.valueOf("")
                id.text = SpannableStringBuilder.valueOf("")

                if (users.size == room.Teams) {
                    bottomDialog.dismiss()
                    if (room.EntryFees >= 10) {
                        callInstamojoPay(
                            email = "test@gmail.com",
                            phone = "6352122123",
                            amount = room.EntryFees.toString(),
                            purpose = room.GameID,
                            buyerName = auth.uid!!
                        )
                    } else {
                        onSuccess()
                    }
                } else {
                    view.sheetDetailNumber.text = "User #${users.size + 1}"
                }
            }

            bottomDialog.setContentView(view)
            bottomDialog.show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(room: Room) {
        if (room.AuthorImage != null) {
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

    private fun callInstamojoPay(
        email: String,
        phone: String,
        amount: String,
        purpose: String,
        buyerName: String
    ) {
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
            pay.put("name", buyerName)
            pay.put("send_sms", false)
            pay.put("send_email", false)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        instamojoPay.start(activity, pay, object : InstapayListener {
            override fun onSuccess(p0: String?) {
                util.showToastMessage(p0.toString())
                onSuccess()
            }

            override fun onFailure(p0: Int, p1: String?) {
                util.showToastMessage(p1!!)
            }
        })
    }

    private fun onSuccess() {
        util.showAlertDialog("Error", "Success")
    }

}
