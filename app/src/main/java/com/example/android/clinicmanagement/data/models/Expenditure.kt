package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenditure_table")
data class Expenditure (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "date_in_seconds")
    val dateInSeconds: Long ,

    @ColumnInfo(name = "expenditure_category")
    val expendCategory: Int,

    @ColumnInfo(name = "amount_spent")
    val amountSpent: Int
)
