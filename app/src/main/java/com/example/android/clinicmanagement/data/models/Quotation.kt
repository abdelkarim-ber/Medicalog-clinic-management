package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo

data class Quotation(
    @ColumnInfo(name = "quotation_number")
    val quoteNumber: Int,

    @ColumnInfo(name = "date_in_seconds")
    val dateInSeconds: Long ,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "doctor_full_name")
    val doctorFullName: String,

    val diagnosis: String,

    val frequency: String,

    @ColumnInfo(name = "session_count")
    val plannedSessions: Int,

    @ColumnInfo(name = "session_price")
    val sessionPrice: Int,

    val total:Int

)
