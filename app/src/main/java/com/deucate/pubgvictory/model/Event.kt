package com.deucate.pubgvictory.model

import com.google.firebase.firestore.DocumentReference

data class Event(
    val id: String,
    val position: String,
    val Ref: DocumentReference
)