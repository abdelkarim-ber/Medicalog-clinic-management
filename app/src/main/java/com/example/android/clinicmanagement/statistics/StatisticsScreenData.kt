package com.example.android.clinicmanagement.statistics

import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth

data class StatisticsScreenData(
    val income: Int?,
    val expenses: Int?,
    val netIncomeByMonthList: List<TotalByMonth>?,
    val expensesByMonthList: List<TotalByMonth>?,
    val expensesByCategoryList: List<TotalSpentByCategory>?
)
