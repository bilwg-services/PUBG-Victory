package com.deucate.pubgvictory.home

import java.io.Serializable

data class Room(
        val GameID: String = "",
        val Title: String = "Title",
        val GameDescription: String = "Not Found",
        val Teams: Int = 1,
        val Time: Long = System.currentTimeMillis(),
        val Image: String = "",
        val Region: String = "Global",
        val AuthorImage: String = "",
        val AuthorName: String = "",
        val Price: Long = 0L,
        val EntryFees: Long = 0L
):Serializable