package com.example.android.clinicmanagement.data.datasources

import com.example.android.clinicmanagement.data.dao.SessionDao
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SessionDataSource (private val sessionDao: SessionDao) {
    suspend fun addSession(session: Session){
        withContext(Dispatchers.IO){
            sessionDao.insert(session)
        }
    }
    suspend fun deleteSession(session: Session){
        withContext(Dispatchers.IO){
            sessionDao.delete(session)
        }
    }
   fun loadSessionsWithPatientID(patientId:Int): Flow<List<Session>> = sessionDao.loadSessionsWithPatientID(patientId)

    suspend fun countSessionsWithPatientID(patientId:Int):Int{
       return withContext(Dispatchers.IO){
            sessionDao.countSessionsWithPatientID(patientId)
        }
    }
    suspend fun getAmountPayedByPatientWithID(patientId:Int):Int{
        return withContext(Dispatchers.IO){
            sessionDao.getAmountPayedByPatientWithID(patientId)
        }
    }
    suspend fun getIncomeForMonth(month: String):Int?{
        return withContext(Dispatchers.IO){
            sessionDao.getIncomeForMonth(month)
        }
    }
    suspend fun loadIncomeStatsForMonthAndNeighbors(month: String):Array<TotalByMonth>{
        return withContext(Dispatchers.IO){
            sessionDao.loadIncomeStatsForMonthAndNeighbors(month)
        }
    }

}