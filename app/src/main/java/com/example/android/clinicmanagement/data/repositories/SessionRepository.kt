package com.example.android.clinicmanagement.data.repositories

import com.example.android.clinicmanagement.data.datasources.SessionDataSource
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SessionRepository(private val sessionDataSource: SessionDataSource) {

    suspend fun addSession(session: Session) =
        sessionDataSource.addSession(session)

    suspend fun deleteSession(session: Session) =
        sessionDataSource.deleteSession(session)

    fun loadSessionsWithPatientID(patientId: Int): Flow<List<Session>> =
        sessionDataSource.loadSessionsWithPatientID(patientId)

    suspend fun countSessionsWithPatientID(patientId: Int): Int =
        sessionDataSource.countSessionsWithPatientID(patientId)

    suspend fun getAmountPayedByPatientWithID(patientId: Int): Int =
        sessionDataSource.getAmountPayedByPatientWithID(patientId)

    suspend fun getIncomeForMonth(month: String): Int? =
        sessionDataSource.getIncomeForMonth(month)

    suspend fun loadIncomeStatsForMonthAndNeighbors(month: String): Array<TotalByMonth> =
        sessionDataSource.loadIncomeStatsForMonthAndNeighbors(month)
}