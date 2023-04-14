package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
data class Session(
    @ColumnInfo(name = "date_in_seconds")
    @PrimaryKey
    val dateInSeconds: Long = System.currentTimeMillis() / 1000,

    @ColumnInfo(name = "amount_payed")
    val amountPayed: Int,

    @ColumnInfo(name = "patient_id")
    val patientId: Long
)
