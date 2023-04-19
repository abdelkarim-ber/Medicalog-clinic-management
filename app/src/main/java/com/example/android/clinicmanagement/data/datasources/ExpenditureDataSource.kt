package com.example.android.clinicmanagement.data.datasources

import com.example.android.clinicmanagement.data.dao.ExpenditureDao
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.models.TotalByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpenditureDataSource (private val expenditureDao: ExpenditureDao) {

    suspend fun addExpenditure(expenditure: Expenditure){
        withContext(Dispatchers.IO){
            expenditureDao.insert(expenditure)
        }
    }
    suspend fun deleteExpenditure(expenditure: Expenditure){
        withContext(Dispatchers.IO){
            expenditureDao.delete(expenditure)
        }
    }
    suspend fun loadTotalByCategoryForMonth(month: String): Array<TotalByCategory>{
        return withContext(Dispatchers.IO){
            expenditureDao.loadTotalByCategoryForMonth(month)
        }
    }
    suspend fun loadExpenditureStatsForMonthAndNeighbors(month: String): Array<TotalByMonth>{
        return withContext(Dispatchers.IO){
            expenditureDao.loadExpenditureStatsForMonthAndNeighbors(month)
        }
    }

}