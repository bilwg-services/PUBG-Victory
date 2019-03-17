package com.deucate.pubgvictory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.utils.Constatns
import com.google.firebase.auth.FirebaseAuth
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
                            rooms.add(
                                Room(
                                    GameID = room.id,
                                    Title = room.getString("Title")!!,
                                    GameDescription = room.getString("GameDescription")!!,
                                    Teams = room.getLong("Teams")!!.toInt(),
                                    Time = room.getLong("Time")!!,
                                    Image = room.getString("Image")!!,
                                    Map = room.getLong("Map")!!.toInt(),
                                    AuthorName = room.getString("AuthorName")!!,
                                    AuthorID = room.getString("AuthorID")!!,
                                    Price = room.getLong("Price")!!,
                                    EntryFees = room.getLong("EntryFees")!!,
                                    AuthorImage = room.getString("AuthorImage")
                                )
                            )
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

    fun <T> MutableLiveData<ArrayList<T>>.add(item: T) {
        val updatedItems = this.value as ArrayList
        updatedItems.add(item)
        this.value = updatedItems
    }

}