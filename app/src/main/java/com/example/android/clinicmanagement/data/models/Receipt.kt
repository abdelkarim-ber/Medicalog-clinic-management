package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo

data class Receipt (

    val receiptNumber: Int,

    @ColumnInfo(name = "date_in_seconds")
    val receiptDateInSeconds: Long ,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    @ColumnInfo(name = "doctor_full_name")
    val doctorFullName: String,

    val diagnosis: String,

    val frequency: String,

    @ColumnInfo(name = "session_count")
    val sessionCount: Int,

    @ColumnInfo(name = "session_price")
    val sessionPrice: Int,

    val total:Int

    )