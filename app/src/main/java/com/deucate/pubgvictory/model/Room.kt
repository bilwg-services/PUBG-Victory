package com.deucate.pubgvictory.model

import java.io.Serializable

data class Room(
    val GameID: String,
    val Title: String,
    val GameDescription: String,
    val Teams: Int,
    val Time: Long,
    val Image: String,
    val Map: Int,
    val AuthorName: String,
    val AuthorID: String,
    val AuthorImage: String?,
    val Price: Long,
    val EntryFees: Long,
    val RoomID: String?,
    val Password: String?
) : Serializable