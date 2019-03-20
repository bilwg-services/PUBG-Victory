package com.deucate.pubgvictory.participate

import android.os.Bundle
import android.app.Activity
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.deucate.pubgvictory.R
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.model.User
import com.deucate.pubgvictory.utils.Constatns
import com.deucate.pubgvictory.utils.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore
import instamojo.library.InstamojoPay
import instamojo.library.InstapayListener
import kotlinx.android.synthetic.main.activity_participate.*

import org.json.JSONException
import org.json.JSONObject

class ParticipateActivity : Activity() {

    private val utils = Util()

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val users = ArrayList<User>()
    private lateinit var currentUser: FirebaseUser

    private val adapter = UserAdapter(users)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participate)

        val recyclerView = participateRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        if (auth.currentUser == null) {
            auth.signInAnonymously().addOnCompleteListener {
                val result = it.result
                if (result != null) {
                    this.currentUser = result.user
                } else {
                    Toast.makeText(this, it.exception!!.localizedMessage, Toast.LENGTH_LONG).show()
                    this.finish()
                }
            }
        } else {
            this.currentUser = auth.currentUser!!
        }

        val parcelRoom = intent.getSerializableExtra("room") as Room?
        if (parcelRoom != null) {
            onGetRoom(parcelRoom)
        } else {
            getRoomFromLink { error, room ->
                if (error == null) {
                    if (room != null) {
                        onGetRoom(room)
                    } else {
                        Toast.makeText(this, "500 internal server error.", Toast.LENGTH_LONG).show()
                        this.finish()
                    }
                } else {
                    Toast.makeText(this, error, Toast.LENGTH_LONG).show()
                    this.finish()
                }
            }
        }
    }

    private fun onGetRoom(room: Room) {
        participateTitle.text = room.Title
        participateButton.setOnClickListener {
            val uid = participateEditText!!.text.toString()
            if (room.Teams <= users.size) {
                callInstamojoPay(
                        email = users[0].Email ?: auth.currentUser?.email ?: "Email not found",
                        phone = users[0].Phone ?: "Phone number not found",
                        amount = room.Price.toString(),
                        purpose = currentUser.uid,
                        buyerName = users[0].Name
                )
                return@setOnClickListener
            }
            getUserFromUID(uid) { error, user ->
                if (error == null) {
                    if (user != null) {
                        addUser(user)
                    } else {
                        AlertDialog.Builder(this).setTitle("Error").setMessage("Something went wrong.").show()
                    }
                } else {
                    AlertDialog.Builder(this).setTitle("Error").setMessage(error).show()
                }
            }
        }
    }

    private fun getUserFromUID(uid: String, callback: (error: String?, user: User?) -> Unit) {

        db.collection(Constatns.users).document(uid).get().addOnSuccessListener {
            if (it != null) {
                callback(null, utils.getUserFromDocument(it))
            } else {
                callback("Something went wrong.", null)
            }
        }.addOnFailureListener {
            callback(it.localizedMessage, null)
        }

    }

    private fun getRoomFromLink(callback: (error: String?, room: Room?) -> Unit) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener { data ->
            if (data != null) {
                Toast.makeText(this, data.link.getQueryParameter("id"), Toast.LENGTH_SHORT)
                        .show()
                utils.getRoomFromID(data.link.getQueryParameter("id") ?: "") { room, error ->
                    if (error == null) {
                        if (room != null) {
                            callback.invoke(null, room)
                        } else {
                            callback.invoke("Event not found.", null)
                        }
                    } else {
                        callback.invoke("error.", null)
                    }
                }
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun callInstamojoPay(email: String, phone: String, amount: String, purpose: String, buyerName: String) {
        Log.d("User:", "Email = $email \n Phone = $phone \n amount = $amount \n purpose = $purpose \n buyer name = $buyerName")
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
                this@ParticipateActivity.onSuccessPayment()
            }

            override fun onFailure(p0: Int, p1: String?) {
                Toast.makeText(this@ParticipateActivity, p1, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onSuccessPayment() {}

    private fun addUser(user: User) {
        users.add(user)
        adapter.notifyItemInserted(users.size)
    }

    override fun finish() {
        super.finish()
        if (auth.currentUser!!.isAnonymous) {
            auth.signOut()
        }
    }

}
