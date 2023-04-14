package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quotation_table")
data class Quotation (
    @ColumnInfo(name = "date_in_seconds")
    @PrimaryKey
    val dateInSeconds: Long = System.currentTimeMillis() / 1000,

    @ColumnInfo(name = "quotation_number")
    val quoteNumber: Int,

    @ColumnInfo(name = "patient_id")
    val patientId: Long
)

