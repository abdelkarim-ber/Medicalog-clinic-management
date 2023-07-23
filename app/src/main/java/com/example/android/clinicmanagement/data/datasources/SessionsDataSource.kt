package com.example.android.clinicmanagement.data.datasources

import androidx.paging.PagingSource
import com.example.android.clinicmanagement.data.dao.SessionsDao
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
    suspend fun getIncomeForMonth(month: String):Int?{
        return withContext(Dispatchers.IO){
            sessionsDao.getIncomeForMonth(month)
        }
    }
    suspend fun loadIncomeStatsForMonthAndNeighbors(month: String):Array<TotalByMonth>?{
        return withContext(Dispatchers.IO){
            sessionsDao.loadIncomeStatsForMonthAndNeighbors(month)
        }
    }

}