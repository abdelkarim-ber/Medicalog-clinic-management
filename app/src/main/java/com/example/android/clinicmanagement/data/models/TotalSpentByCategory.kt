package com.example.android.clinicmanagement.data.models

import androidx.room.ColumnInfo

data class TotalSpentByCategory(
    @ColumnInfo(name = "expenditure_category") val expendCategory:Int,
    val total: Int
)
