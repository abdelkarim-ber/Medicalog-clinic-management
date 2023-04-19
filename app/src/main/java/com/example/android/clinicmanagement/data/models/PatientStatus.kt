package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo

data class PatientStatus(
    val id: Long,
    @ColumnInfo(name = "first_name") val firstName:String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @ColumnInfo(name = "invoice_number") val invNumber:Int?
)
