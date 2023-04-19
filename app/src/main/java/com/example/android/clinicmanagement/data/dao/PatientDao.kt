package com.example.android.clinicmanagement.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.models.PatientStatus

//Here we select invoice_number to know the status of patient: In progress or done
const val GET_PATIENTS_WITH_STATUS =
    "SELECT id,first_name,last_name,invoice_number FROM patient_table AS p " +
            "LEFT JOIN invoice_track_table AS i ON p.id = i.patient_id "

const val COUNT_PATIENTS_WITH_STATUS =
    "SELECT count(*) FROM patient_table AS p " +
            "LEFT JOIN invoice_track_table AS i ON p.id = i.patient_id "

@Dao
interface PatientDao {
    @Insert
    suspend fun insert(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Update
    suspend fun update(patient: Patient)

    @Query(
        "SELECT * FROM patient_table" +
                " WHERE id = :patientId"
    )
    suspend fun loadPatientWithId(patientId:Long): Patient

    @Query(
        GET_PATIENTS_WITH_STATUS +
                " ORDER BY consultation_date_seconds DESC"
    )
    fun loadAllPatientsStatus(): PagingSource<Int, PatientStatus>

    @Query(
        GET_PATIENTS_WITH_STATUS +
                " WHERE first_name LIKE '%' || :firstName || '%'"
    )
    fun findPatientsWithFirstName(firstName: String): PagingSource<Int, PatientStatus>

    @Query(
        GET_PATIENTS_WITH_STATUS +
                " WHERE last_name LIKE '%' || :lastName || '%'"
    )
    fun findPatientsWithLastName(lastName: String): PagingSource<Int, PatientStatus>

    @Query(
        GET_PATIENTS_WITH_STATUS +
                " WHERE date(consultation_date_seconds,'unixepoch') BETWEEN :startDate AND :endDate " +
                "ORDER BY consultation_date_seconds "
    )
    fun findPatientsWithConsultDateRange(
        startDate: String,
        endDate: String
    ): PagingSource<Int, PatientStatus>

    @Query(
        GET_PATIENTS_WITH_STATUS +
                " WHERE age BETWEEN :startAge AND :endAge " +
                "ORDER BY age "
    )
    fun findPatientsWithAgeRange(startAge: Int, endAge: Int): PagingSource<Int, PatientStatus>


    @Query(
        COUNT_PATIENTS_WITH_STATUS +
                " WHERE first_name LIKE '%' || :firstName || '%'"
    )
    suspend fun countPatientsWithFirstName(firstName: String): Int

    @Query(
        COUNT_PATIENTS_WITH_STATUS +
                " WHERE last_name LIKE '%' || :lastName || '%'"
    )
    suspend fun countPatientsWithLastName(lastName: String): Int

    @Query(
        COUNT_PATIENTS_WITH_STATUS +
                " WHERE date(consultation_date_seconds,'unixepoch') BETWEEN :startDate AND :endDate "
    )
    suspend fun countPatientsWithConsultDateRange(
        startDate: String,
        endDate: String
    ): Int

    @Query(
        COUNT_PATIENTS_WITH_STATUS +
                " WHERE age BETWEEN :startAge AND :endAge "
    )
    suspend fun countPatientsWithAgeRange(
        startAge: Int,
        endAge: Int
    ): Int


}