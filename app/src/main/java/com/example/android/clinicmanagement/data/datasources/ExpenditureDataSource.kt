package com.example.android.clinicmanagement.data.datasources

import androidx.paging.PagingSource
import com.example.android.clinicmanagement.data.dao.ExpenditureDao
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.Dispatchers
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


    suspend fun loadTotalByCategoryForMonth(month: String): Array<TotalSpentByCategory>? {
        return withContext(Dispatchers.IO) {
            expenditureDao.loadTotalByCategoryForMonth(month)
        }
    }

    suspend fun getExpensesForMonth(month: String): Int? {
        return withContext(Dispatchers.IO) {
            expenditureDao.getExpensesForMonth(month)
        }
    }

    suspend fun loadExpenditureStatsForMonthAndNeighbors(month: String): Array<TotalByMonth>? {
        return withContext(Dispatchers.IO) {
            expenditureDao.loadExpenditureStatsForMonthAndNeighbors(month)
        }
    }

}