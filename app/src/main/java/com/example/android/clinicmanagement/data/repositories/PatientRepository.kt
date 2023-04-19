package com.example.android.clinicmanagement.data.repositories

import androidx.paging.PagingSource
import com.example.android.clinicmanagement.data.datasources.PatientDataSource
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.models.PatientStatus

class PatientRepository(private val patientDataSource: PatientDataSource) {

    suspend fun addPatient(patient: Patient) = patientDataSource.addPatient(patient)

    suspend fun deletePatient(patient: Patient) = patientDataSource.deletePatient(patient)

    suspend fun updatePatient(patient: Patient) = patientDataSource.updatePatient(patient)

    suspend fun loadPatientWithId(patientId: Long): Patient =
        patientDataSource.loadPatientWithId(patientId)

    fun loadAllPatientsStatus(): PagingSource<Int, PatientStatus> =
        patientDataSource.loadAllPatientsStatus()

    fun findPatientsWithFirstName(firstName: String): PagingSource<Int, PatientStatus> =
        patientDataSource.findPatientsWithFirstName(firstName)

    fun findPatientsWithLastName(lastName: String): PagingSource<Int, PatientStatus> =
        patientDataSource.findPatientsWithLastName(lastName)

    fun findPatientsWithConsultDateRange(
        startDate: String,
        endDate: String
    ): PagingSource<Int, PatientStatus> =
        patientDataSource.findPatientsWithConsultDateRange(startDate, endDate)

    fun findPatientsWithAgeRange(startAge: Int, endAge: Int): PagingSource<Int, PatientStatus> =
        patientDataSource.findPatientsWithAgeRange(startAge, endAge)

    suspend fun countPatientsWithFirstName(firstName: String): Int =
        patientDataSource.countPatientsWithFirstName(firstName)

    suspend fun countPatientsWithLastName(lastName: String): Int =
        patientDataSource.countPatientsWithLastName(lastName)

    suspend fun countPatientsWithConsultDateRange(startDate: String, endDate: String): Int =
        patientDataSource.countPatientsWithConsultDateRange(startDate, endDate)

    suspend fun countPatientsWithAgeRange(startAge: Int, endAge: Int): Int =
        patientDataSource.countPatientsWithAgeRange(startAge, endAge)


}