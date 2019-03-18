package com.deucate.pubgvictory.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Event(
    val id: String,
    val Ref: DocumentReference,
    val Time: Timestamp,
    val Title: String,
    val Price: Long
)