package com.example.android.clinicmanagement.data.repositories

import com.example.android.clinicmanagement.data.datasources.ExpenditureDataSource
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth

class ExpenditureRepository(private val expenditureDataSource: ExpenditureDataSource) {
    suspend fun addExpenditure(expenditure: Expenditure) =
        expenditureDataSource.addExpenditure(expenditure)

    suspend fun deleteExpenditure(expenditure: Expenditure) =
        expenditureDataSource.deleteExpenditure(expenditure)

    suspend fun loadTotalByCategoryForMonth(month: String): Array<TotalSpentByCategory>? =
        expenditureDataSource.loadTotalByCategoryForMonth(month)

    suspend fun getExpensesForMonth(month: String):Int? =
        expenditureDataSource.getExpensesForMonth(month)

    suspend fun loadExpenditureStatsForMonthAndNeighbors(month: String): Array<TotalByMonth>? =
        expenditureDataSource.loadExpenditureStatsForMonthAndNeighbors(month)
}