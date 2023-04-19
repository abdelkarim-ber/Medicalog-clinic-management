package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo

data class Invoice(

    @ColumnInfo(name = "date_in_seconds")
    val dateInSeconds: Long ,

    @ColumnInfo(name = "invoice_number")
    val invNumber: Int,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    val total:Int,

    val doneSessions:Int
)
