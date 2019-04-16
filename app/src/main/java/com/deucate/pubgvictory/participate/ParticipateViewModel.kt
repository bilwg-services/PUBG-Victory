package com.deucate.pubgvictory.participate

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.model.User
import com.deucate.pubgvictory.utils.Constatns
import com.deucate.pubgvictory.utils.Util
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.firestore.FirebaseFirestore

class ParticipateViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val util = Util()

    private val participants = MutableLiveData<ArrayList<User>>()
    private lateinit var currentUser: FirebaseUser

    val error = MutableLiveData<String>()

    init {
        initializeUser()
        initializeData()
    }

    private fun initializeUser() {
        if (auth.currentUser == null) {
            auth.signInAnonymously().addOnCompleteListener {
                val result = it.result
                if (result != null) {
                    this.currentUser = result.user
                } else {
                    error.value = it.exception!!.localizedMessage
                }
            }
        } else {
            this.currentUser = auth.currentUser!!
        }
    }

    private fun initializeData() {
        this.participants.value = ArrayList()
    }

    fun getUserFromUID(uid: String, callback: (error: String?, user: User?) -> Unit) {
        db.collection(Constatns.users).document(uid).get().addOnSuccessListener {
            if (it != null) {
                val user = util.getUserFromDocument(it)
                addUser(user)
                callback(null, user)
            } else {
                callback("Something went wrong.", null)
            }
        }.addOnFailureListener {
            callback(it.localizedMessage, null)
        }
    }

    fun getRoomFromLink(intent: Intent, callback: (error: String?, room: Room?) -> Unit) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener { data ->
            if (data != null) {
                util.getRoomFromID(data.link.getQueryParameter("id") ?: "") { room, error ->
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
                error.value = "Something went wrong"
            }
        }.addOnFailureListener {
            error.value = it.localizedMessage
        }
    }

    private fun addUser(user: User) {
        participants.value!!.add(user)
    }

}