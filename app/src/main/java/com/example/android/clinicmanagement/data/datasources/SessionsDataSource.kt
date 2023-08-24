package com.example.android.clinicmanagement.data.datasources

import androidx.paging.PagingSource
import com.example.android.clinicmanagement.data.dao.SessionsDao
import com.example.android.clinicmanagement.data.models.IncomeAndExpenses
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SessionsDataSource (private val sessionsDao: SessionsDao) {
    suspend fun addSession(session: Session){
        withContext(Dispatchers.IO){
            sessionsDao.insert(session)
        }
    }
    suspend fun deleteSession(session: Session){
        withContext(Dispatchers.IO){
            sessionsDao.delete(session)
        }
    }
   fun loadSessionsWithPatientID(patientId:Long): PagingSource<Int, Session> = sessionsDao.loadSessionsWithPatientID(patientId)

//    suspend fun countSessionsWithPatientID(patientId:Int):Int{
//       return withContext(Dispatchers.IO){
//            sessionsDao.countSessionsWithPatientID(patientId)
//        }
//    }
//    suspend fun getAmountPayedByPatientWithID(patientId:Int):Int{
//        return withContext(Dispatchers.IO){
//            sessionsDao.getAmountPayedByPatientWithID(patientId)
//        }
//    }
    fun getIncomeForMonth(month: String):Flow<Int?> =
            sessionsDao.getIncomeForMonth(month)

    fun loadIncomeStatsForMonthAndNeighbors(month: String):Flow<Array<TotalByMonth>?> =
            sessionsDao.loadIncomeStatsForMonthAndNeighbors(month)

    suspend fun loadFinancesForMonth(month: String): IncomeAndExpenses {
        return withContext(Dispatchers.IO){
            sessionsDao.loadFinancesForMonth(month)
        }
    }
}