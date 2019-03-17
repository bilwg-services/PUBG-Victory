package com.deucate.pubgvictory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Constatns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val error = MutableLiveData<String>()

    val rooms: MutableLiveData<ArrayList<Room>> by lazy {
        MutableLiveData<ArrayList<Room>>().also {
            loadRooms()
        }
    }

    init {
        rooms.value = ArrayList()
    }

    private fun loadRooms() {
        db.collection(Constatns.rooms)
            .get().addOnCompleteListener {
                if (it.exception == null) {
                    if (it.result != null || !it.result!!.isEmpty) {
                        for (room in it.result!!.documents) {
                            rooms.add(getRoomFromDocument(room))
                        }
                        Log.d("--->", rooms.value.toString())
                    } else {
                        error.value = "500 Internal server error."
                    }
                } else {
                    error.value = it.exception!!.localizedMessage
                }
            }
    }


    fun searchRoom(name: String, callback: (ArrayList<Room>?) -> Unit) {
        db.collection("Rooms").orderBy("Title").startAt(name).endAt(name + '\uf8ff').get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val searchResult = ArrayList<Room>()
                    for (doc in it.result!!.documents) {
                        searchResult.add(getRoomFromDocument(doc))
                    }
                    callback(searchResult)
                } else {
                    callback(null)
                }
            }
    }

    private fun getRoomFromDocument(document: DocumentSnapshot): Room {
        return Room(
            GameID = document.id,
            Title = document.getString("Title")!!,
            GameDescription = document.getString("GameDescription")!!,
            Teams = document.getLong("Teams")!!.toInt(),
            Time = document.getLong("Time")!!,
            Image = document.getString("Image")!!,
            Map = document.getLong("Map")!!.toInt(),
            AuthorName = document.getString("AuthorName")!!,
            AuthorID = document.getString("AuthorID")!!,
            Price = document.getLong("Price")!!,
            EntryFees = document.getLong("EntryFees")!!,
            AuthorImage = document.getString("AuthorImage")
        )
    }

    fun <T> MutableLiveData<ArrayList<T>>.add(item: T) {
        val updatedItems = this.value as ArrayList
        updatedItems.add(item)
        this.value = updatedItems
    }

}