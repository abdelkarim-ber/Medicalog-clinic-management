package com.example.android.clinicmanagement.data.repositories

import com.example.android.clinicmanagement.data.datasources.SessionsDataSource
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.flow.Flow

class SessionsRepository(private val sessionsDataSource: SessionsDataSource) {

    suspend fun addSession(session: Session) =
        sessionsDataSource.addSession(session)

    suspend fun deleteSession(session: Session) =
        sessionsDataSource.deleteSession(session)

    fun loadSessionsWithPatientID(patientId: Int): Flow<List<Session>> =
        sessionsDataSource.loadSessionsWithPatientID(patientId)

    suspend fun countSessionsWithPatientID(patientId: Int): Int =
        sessionsDataSource.countSessionsWithPatientID(patientId)

    suspend fun getAmountPayedByPatientWithID(patientId: Int): Int =
        sessionsDataSource.getAmountPayedByPatientWithID(patientId)

    suspend fun getIncomeForMonth(month: String): Int? =
        sessionsDataSource.getIncomeForMonth(month)

    suspend fun loadIncomeStatsForMonthAndNeighbors(month: String): Array<TotalByMonth>? =
        sessionsDataSource.loadIncomeStatsForMonthAndNeighbors(month)
}