package com.deucate.pubgvictory.utils

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Toast
import com.deucate.pubgvictory.model.Event
import com.deucate.pubgvictory.model.Room
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
            RoomID = document.getString("RoomID")
        )
    }

    fun getEventFromDocument(event: DocumentSnapshot): Event {
        return Event(
            id = event.id,
            RoomRef = event.getDocumentReference("RoomRef")!!,
            Time = event.getTimestamp("Time")!!,
            Title = event.getString("Title") ?: "No title Found",
            Price = event.getLong("Price") ?: 0,
            TeamRef = event.getDocumentReference("TeamRef")!!
        )
    }


}