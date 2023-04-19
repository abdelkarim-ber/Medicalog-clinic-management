package com.example.android.clinicmanagement.data.repositories

import com.example.android.clinicmanagement.data.datasources.ExpenditureDataSource
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.TotalByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenditureRepository(private val expenditureDataSource: ExpenditureDataSource) {
    suspend fun addExpenditure(expenditure: Expenditure) =
        expenditureDataSource.addExpenditure(expenditure)

    suspend fun deleteExpenditure(expenditure: Expenditure) =
        expenditureDataSource.deleteExpenditure(expenditure)

    suspend fun loadTotalByCategoryForMonth(month: String): Array<TotalByCategory> =
        expenditureDataSource.loadTotalByCategoryForMonth(month)

    suspend fun loadExpenditureStatsForMonthAndNeighbors(month: String): Array<TotalByMonth> =
        expenditureDataSource.loadExpenditureStatsForMonthAndNeighbors(month)
}