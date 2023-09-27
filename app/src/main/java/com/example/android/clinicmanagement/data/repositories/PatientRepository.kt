package com.example.android.clinicmanagement.data.repositories

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.android.clinicmanagement.data.datasources.PatientDataSource
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.models.PatientDetails
import com.example.android.clinicmanagement.data.models.PatientStatus
import com.example.android.clinicmanagement.patientsList.FilterDataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


private const val ITEMS_PER_PAGE = 10
private const val MAX_ITEMS = 50


class PatientRepository(private val patientDataSource: PatientDataSource) {

    suspend fun addPatient(patient: Patient) = patientDataSource.addPatient(patient)

    suspend fun deletePatient(patient: Patient) = patientDataSource.deletePatient(patient)

    suspend fun updatePatient(patient: Patient) = patientDataSource.updatePatient(patient)

    suspend fun loadPatientWithId(patientId: Long): Patient =
        patientDataSource.loadPatientWithId(patientId)

    fun loadPatientsStatusWithFilter(
        filterDataState:FilterDataState
    ): LiveData<PagingData<PatientStatus>> {

        val patientsPagingSourceFactory = {
            patientDataSource.loadPatientsStatusWithFilter(
                filterDataState.firstName,
                filterDataState.lastName,
                filterDataState.consultDateRangeStart,
                filterDataState.consultDateRangeEnd,
                filterDataState.ageRangeStart,
                filterDataState.ageRangeEnd,
                filterDataState.diagnosis,
                filterDataState.gender,
                filterDataState.sessionsCompletionState
            )
        }

        return Pager(
            config = PagingConfig(
                initialLoadSize = ITEMS_PER_PAGE * 2,
                maxSize = MAX_ITEMS,
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = patientsPagingSourceFactory
        ).liveData

    }


    suspend fun countPatientsStatusWithFilter(
        filterDataState:FilterDataState
    ): Int {
        return patientDataSource.countPatientsStatusWithFilter(
            filterDataState.firstName,
            filterDataState.lastName,
            filterDataState.consultDateRangeStart,
            filterDataState.consultDateRangeEnd,
            filterDataState.ageRangeStart,
            filterDataState.ageRangeEnd,
            filterDataState.diagnosis,
            filterDataState.gender,
            filterDataState.sessionsCompletionState
            )
    }

    fun loadPatientDetailsWithId(patientId: Long):Flow<PatientDetails> = patientDataSource.loadPatientDetailsWithId(patientId)

    fun invoiceExistsForPatientWithId(patientId: Long): Flow<Boolean> = patientDataSource.invoiceExistsForPatientWithId(patientId).map { it == 1 }
}