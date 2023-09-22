package com.example.android.clinicmanagement.data.datasources

import androidx.paging.PagingSource
import com.example.android.clinicmanagement.data.dao.PatientDao
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.models.PatientDetails
import com.example.android.clinicmanagement.data.models.PatientStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PatientDataSource(private val patientDao: PatientDao) {

    suspend fun addPatient(patient: Patient) {
        withContext(Dispatchers.IO) {
            patientDao.insert(patient)
        }
    }

    suspend fun deletePatient(patient: Patient) {
        withContext(Dispatchers.IO) {
            patientDao.delete(patient)
        }
    }

    suspend fun updatePatient(patient: Patient) {
        withContext(Dispatchers.IO) {
            patientDao.update(patient)
        }
    }

    suspend fun loadPatientWithId(patientId: Long): Patient {
        return withContext(Dispatchers.IO) {
            patientDao.loadPatientWithId(patientId)
        }
    }

    fun loadPatientsStatusWithFilter(
        firstName: String?,
        lastName: String?,
        startConsultDate: String?,
        endConsultDate: String?,
        startAge: Int?,
        endAge: Int?,
        diagnosis: String?,
        gender: Char?,
        completion: Int?
    ): PagingSource<Int, PatientStatus> {
        return patientDao.loadPatientsStatusWithFilter(
            firstName,
            lastName,
            startConsultDate,
            endConsultDate,
            startAge,
            endAge,
            diagnosis,
            gender,
            completion
        )
    }


    suspend fun countPatientsStatusWithFilter(
        firstName: String?,
        lastName: String?,
        startConsultDate: String?,
        endConsultDate: String?,
        startAge: Int?,
        endAge: Int?,
        diagnosis: String?,
        gender: Char?,
        completion: Int?
    ): Int {
        return withContext(Dispatchers.IO) {
            patientDao.countPatientsStatusWithFilter(
                firstName,
                lastName,
                startConsultDate,
                endConsultDate,
                startAge,
                endAge,
                diagnosis,
                gender,
                completion
            )
        }
    }

    fun loadPatientDetailsWithId(patientId: Long): Flow<PatientDetails> = patientDao.loadPatientDetailsWithId(patientId)

    fun invoiceExistsForPatientWithId(patientId: Long): Flow<Int> = patientDao.invoiceExistsForPatientWithId(patientId)
}