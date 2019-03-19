package com.deucate.pubgvictory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.deucate.pubgvictory.model.Event
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.model.User
import com.deucate.pubgvictory.utils.Constatns
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val error = MutableLiveData<String>()

    val rooms = MutableLiveData<ArrayList<Room>>()
    val user = MutableLiveData<User>()
    val myEvents = MutableLiveData<ArrayList<Event>>()

    init {
        rooms.value = ArrayList()
        myEvents.value = ArrayList()
        user.value = User(
            ID = auth.uid!!,
            Name = auth.currentUser!!.displayName!!,
            Phone = auth.currentUser!!.phoneNumber!!,
            Email = auth.currentUser!!.email!!
        )

        loadUser()
        loadRooms()
        loadMyEvents()
    }

    private fun loadRooms() {
        db.collection(Constatns.rooms)
            .get().addOnCompleteListener {
                if (it.exception == null) {
                    if (it.result != null || !it.result!!.isEmpty) {
                        for (room in it.result!!.documents) {
                            rooms.add(getRoomFromDocument(room))
                        }
                    } else {
                        error.value = "500 Internal server error."
                    }
                } else {
                    error.value = it.exception!!.localizedMessage
                }
            }
    }

    private fun loadUser() {
        db.collection(Constatns.users).document(auth.uid!!).get().addOnCompleteListener {
            if (it.isSuccessful) {
                it.result.let { user ->
                    this.user.value = User(
                        ID = user!!.id,
                        Name = user.getString("Name")!!,
                        Phone = user.getString("Phone")!!,
                        Email = user.getString("Email")!!
                    )
                }
            } else {
                error.value = it.exception!!.localizedMessage
            }
        }
    }

    private fun loadMyEvents() {
        db.collection(Constatns.users).document(auth.currentUser!!.uid).collection("Matches").get()
            .addOnCompleteListener {
                if (it.exception == null) {
                    if (it.result != null || !it.result!!.isEmpty) {
                        for (myEvent in it.result!!.documents) {
                            myEvents.add(getEventFromDocument(myEvent))
                        }
                    } else {
                        error.value = "500 Internal server error."
                    }
                } else {
                    error.value = it.exception!!.localizedMessage
                }
            }
    }

    private fun getEventFromDocument(event: DocumentSnapshot): Event {
        return Event(
            id = event.id,
            RoomRef = event.getDocumentReference("RoomRef")!!,
            Time = event.getTimestamp("Time")!!,
            Title = event.getString("Title") ?: "No title Found",
            Price = event.getLong("Price") ?: 0,
            TeamRef = event.getDocumentReference("TeamRef")!!
        )
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

    fun getRoomFromDocument(document: DocumentSnapshot): Room {
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
            AuthorImage = document.getString("AuthorImage"),
            RoomID = document.getString("RoomID")
        )
    }

    private fun <T> MutableLiveData<ArrayList<T>>.add(item: T) {
        val updatedItems = this.value as ArrayList
        updatedItems.add(item)
        this.value = updatedItems
    }

}