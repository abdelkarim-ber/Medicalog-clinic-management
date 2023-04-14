package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "patient_table")
data class Patient(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "first_name")
    val firstName: String,

    @ColumnInfo(name = "last_name")
    val lastName: String,

    val age: Int,

    val gender: Char,

    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,

    @ColumnInfo(name = "consultation_date_seconds")
    val consultationDateInSeconds: Long = System.currentTimeMillis() / 1000,

    @ColumnInfo(name = "doctor_full_name")
    val doctorFullName: String,

    val diagnosis: String,

    val frequency: String,

    @ColumnInfo(name = "session_count")
    val sessionCount: Int,

    @ColumnInfo(name = "session_price")
    val sessionPrice: Int,


    @ColumnInfo(name = "social_coverage")
    val socialCoverage: String
)

