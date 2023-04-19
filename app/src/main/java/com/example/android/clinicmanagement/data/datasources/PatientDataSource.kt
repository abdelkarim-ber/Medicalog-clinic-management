package com.example.android.clinicmanagement.data.datasources

import androidx.paging.PagingSource
import com.example.android.clinicmanagement.data.dao.PatientDao
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.models.PatientStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PatientDataSource(private val patientDao: PatientDao ) {

    suspend fun addPatient(patient: Patient){
        withContext(Dispatchers.IO){
            patientDao.insert(patient)
        }
    }
    suspend fun deletePatient(patient: Patient){
        withContext(Dispatchers.IO){
            patientDao.delete(patient)
        }
    }
    suspend fun updatePatient(patient: Patient){
        withContext(Dispatchers.IO){
            patientDao.update(patient)
        }
    }
    suspend fun loadPatientWithId(patientId:Long): Patient {
        return withContext(Dispatchers.IO){
            patientDao.loadPatientWithId(patientId)
        }
    }

    fun loadAllPatientsStatus(): PagingSource<Int, PatientStatus> = patientDao.loadAllPatientsStatus()

    fun findPatientsWithFirstName(firstName:String): PagingSource<Int, PatientStatus> = patientDao.findPatientsWithFirstName(firstName)

    fun findPatientsWithLastName(lastName:String): PagingSource<Int, PatientStatus> = patientDao.findPatientsWithLastName(lastName)

    fun findPatientsWithConsultDateRange(startDate: String,endDate: String): PagingSource<Int, PatientStatus> = patientDao.findPatientsWithConsultDateRange(startDate, endDate)

    fun findPatientsWithAgeRange(startAge: Int, endAge: Int): PagingSource<Int, PatientStatus> = patientDao.findPatientsWithAgeRange(startAge, endAge)

    suspend fun countPatientsWithFirstName(firstName: String): Int {
        return withContext(Dispatchers.IO){
            patientDao.countPatientsWithFirstName(firstName)
        }
    }
    suspend fun countPatientsWithLastName(lastName: String): Int {
        return withContext(Dispatchers.IO){
            patientDao.countPatientsWithLastName(lastName)
        }
    }
    suspend fun countPatientsWithConsultDateRange(startDate: String, endDate: String): Int {
        return withContext(Dispatchers.IO){
            patientDao.countPatientsWithConsultDateRange(startDate,endDate)
        }
    }
    suspend fun countPatientsWithAgeRange(startAge: Int, endAge: Int): Int {
        return withContext(Dispatchers.IO){
            patientDao.countPatientsWithAgeRange(startAge, endAge)
        }
    }
}