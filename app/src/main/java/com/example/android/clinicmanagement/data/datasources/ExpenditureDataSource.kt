package com.example.android.clinicmanagement.data.datasources

import androidx.paging.PagingSource
import com.example.android.clinicmanagement.data.dao.ExpenditureDao
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ExpenditureDataSource(private val expenditureDao: ExpenditureDao) {

    suspend fun addExpenditure(expenditure: Expenditure) {
        withContext(Dispatchers.IO) {
            expenditureDao.insert(expenditure)
        }
    }

    suspend fun deleteExpenditure(expenditure: Expenditure) {
        withContext(Dispatchers.IO) {
            expenditureDao.delete(expenditure)
        }
    }

    fun loadExpensesOrderedBy(order: Int): PagingSource<Int, Expenditure> =
        expenditureDao.loadExpensesOrderedBy(order)


    fun loadExpensesByCategoryForMonth(month: String): Flow<Array<TotalSpentByCategory>?> =
            expenditureDao.loadExpensesByCategoryForMonth(month)


    fun getExpensesForMonth(month: String): Flow<Int?> =
            expenditureDao.getExpensesForMonth(month)


    fun loadExpenditureStatsForMonthAndNeighbors(month: String): Flow<Array<TotalByMonth>?> =
            expenditureDao.loadExpenditureStatsForMonthAndNeighbors(month)


}