package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo

data class Quotation(
    @ColumnInfo(name = "date_in_seconds")
    val dateInSeconds: Long ,

    @ColumnInfo(name = "quotation_number")
    val quoteNumber: Int,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    val total:Int,

    @ColumnInfo(name = "session_count")
    val sessionCount: Int
)
