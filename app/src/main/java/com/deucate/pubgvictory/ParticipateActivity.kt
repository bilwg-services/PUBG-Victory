package com.deucate.pubgvictory

import android.os.Bundle
import android.app.Activity
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.deucate.pubgvictory.model.Room
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import instamojo.library.InstamojoPay
import instamojo.library.InstapayListener

import org.json.JSONException
import org.json.JSONObject

class ParticipateActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participate)

        val room = intent.getSerializableExtra("room") as Room?
        if (room != null) {
            Toast.makeText(this, room.toString(), Toast.LENGTH_SHORT).show()
        } else {
            FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener { data ->
                if (data != null) {
                    Log.d("----->",data.toString())
                    val deepLink = data.link.getQueryParameter("id")
                    Log.d("----->", deepLink)
                    Toast.makeText(this, data.link.toString(), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun onSuccess() {}

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
                this@ParticipateActivity.onSuccess()
            }

            override fun onFailure(p0: Int, p1: String?) {
                Toast.makeText(this@ParticipateActivity, p1, Toast.LENGTH_SHORT).show()
            }
        })
    }

}
