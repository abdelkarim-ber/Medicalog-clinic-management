package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "invoice_track_table")
data class InvoiceTrack (
    @ColumnInfo(name = "date_in_seconds")
    @PrimaryKey
    val dateInSeconds: Long = System.currentTimeMillis() / 1000,

    @ColumnInfo(name = "invoice_number")
    val invNumber: Int,

    @ColumnInfo(name = "patient_id")
    val patientId: Long
)
