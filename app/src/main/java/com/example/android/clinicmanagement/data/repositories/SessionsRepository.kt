package com.example.android.clinicmanagement.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.clinicmanagement.data.datasources.SessionsDataSource
import com.example.android.clinicmanagement.data.models.PatientStatus
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.models.TotalByMonth
import com.example.android.clinicmanagement.patientsList.FilterDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

private const val ITEMS_PER_PAGE = 10
private const val MAX_ITEMS = 50

class SessionsRepository(private val sessionsDataSource: SessionsDataSource) {

    suspend fun addSession(session: Session) =
        sessionsDataSource.addSession(session)

    suspend fun deleteSession(session: Session) =
        sessionsDataSource.deleteSession(session)


    fun loadSessionsWithPatientID(patientId: Long): Flow<PagingData<Session>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = ITEMS_PER_PAGE * 2,
                maxSize = MAX_ITEMS,
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                sessionsDataSource.loadSessionsWithPatientID(patientId)

            }
        ).flow

    }

//    suspend fun countSessionsWithPatientID(patientId: Int): Int =
//        sessionsDataSource.countSessionsWithPatientID(patientId)
//
//    suspend fun getAmountPayedByPatientWithID(patientId: Int): Int =
//        sessionsDataSource.getAmountPayedByPatientWithID(patientId)

    fun getIncomeForMonth(month: String): Flow<Int?> =
        sessionsDataSource.getIncomeForMonth(month)

    fun loadIncomeStatsForMonthAndNeighbors(month: String): Flow<Array<TotalByMonth>?> =
        sessionsDataSource.loadIncomeStatsForMonthAndNeighbors(month)


    suspend fun isDataAvailableForMonth(month: String): Boolean {
        return withContext(Dispatchers.IO) {
            val (income, expenses) = sessionsDataSource.loadFinancesForMonth(month)
            //If one at least is greater than 0 then there is some data to show for
            //the given month.
            (income != null && income != 0) || (expenses != null && expenses != 0)

        }
    }
}