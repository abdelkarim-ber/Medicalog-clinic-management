package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "session_table")
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "date_in_seconds")
    val dateInSeconds: Long ,

    @ColumnInfo(name = "amount_payed")
    val amountPayed: Int,

    @ColumnInfo(name = "patient_id")
    val patientId: Long
)
