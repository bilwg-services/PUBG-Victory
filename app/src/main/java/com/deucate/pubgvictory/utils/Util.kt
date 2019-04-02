package com.deucate.pubgvictory.utils

import android.annotation.SuppressLint
import com.deucate.pubgvictory.model.Event
import com.deucate.pubgvictory.model.Room
import com.deucate.pubgvictory.model.User
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class Util {


    @SuppressLint("SimpleDateFormat")
    fun getFormattedDate(smsTimeInMilis: Long): String {
        val smsTime = Calendar.getInstance()
        smsTime.timeInMillis = smsTimeInMilis

        val now = Calendar.getInstance()

        return when {
            now.get(Calendar.DATE) == smsTime.get(Calendar.DATE) -> "Today"

            now.get(Calendar.DATE) + smsTime.get(Calendar.DATE) == 1 -> "Tomorrow"

            else -> SimpleDateFormat("dd/MM/yyyy").format(Date(smsTimeInMilis))
        }
    }

    fun getTypeOfTeam(team: Int): String {
        return when (team) {
            1 -> "Solo"
            2 -> "Duo"
            4 -> "Squad"
            else -> "Undefined"
        }
    }

    fun getRoomFromID(id: String, callbacks: (room: Room?, error: String?) -> Unit) {
        FirebaseFirestore.getInstance().collection(Constatns.rooms).document(id).get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val result = it.result
                        if (result != null && result.exists()) {
                            val room = getRoomFromDocument(result)
                            callbacks.invoke(room, null)
                        } else {
                            callbacks.invoke(null, "404 not found")
                        }
                    } else {
                        callbacks.invoke(null, it.exception!!.localizedMessage)
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
                RoomID = document.getString("RoomID"),
                Password = document.getString("Password")
        )
    }

    fun getUserFromDocument(document: DocumentSnapshot): User {
        return User(
                ID = document.id,
                Name = document.getString("Name") ?: "Not found",
                Phone = document.getString("Phone"),
                Email = document.getString("Email")
        )
    }

    fun getEventFromDocument(document: DocumentSnapshot): Event {
        return Event(
                id = document.id,
                RoomRef = document.getDocumentReference("RoomRef")!!,
                Time = document.getTimestamp("Time")!!,
                Title = document.getString("Title") ?: "No title Found",
                Price = document.getLong("Price") ?: 0,
                TeamRef = document.getDocumentReference("TeamRef")!!
        )
    }


}