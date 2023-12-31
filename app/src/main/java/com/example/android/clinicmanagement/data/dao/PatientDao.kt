package com.example.android.clinicmanagement.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.models.PatientDetails
import com.example.android.clinicmanagement.data.models.PatientStatus
import kotlinx.coroutines.flow.Flow

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
    suspend fun loadPatientWithId(patientId: Long): Patient

    //------------------------------------------------------
    @Query(
        "SELECT P.id,first_name,last_name,invoice_number,gender,age,diagnosis,max(S.date_in_seconds) as lastSession FROM patient_table AS P " +
                "LEFT JOIN invoice_track_table AS I ON P.id = I.patient_id " +
                "LEFT JOIN session_table AS S ON S.patient_id = P.id " +

                "WHERE (:firstName IS NULL OR (first_name LIKE '%' || :firstName || '%') )" +
                " AND ( :lastName IS NULL OR (last_name LIKE '%' || :lastName || '%')  ) " +
                " AND (:startConsultDate IS NULL OR (date(consultation_date_seconds,'unixepoch') BETWEEN :startConsultDate AND :endConsultDate)) " +
                " AND (:startAge  IS NULL OR ( age BETWEEN :startAge AND :endAge)) " +
                " AND (:gender IS NULL OR gender = :gender) " +
                " AND ( :diagnosis  IS NULL OR (diagnosis LIKE '%' || :diagnosis || '%') )" +
                " AND ( :completion  IS NULL OR (CASE WHEN :completion = 0 THEN invoice_number IS NULL ELSE invoice_number IS NOT NULL END) ) " +
                "GROUP BY P.id " +
                "ORDER BY lastSession DESC "
    )
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
    ): PagingSource<Int, PatientStatus>

    @Query(
        "SELECT count(*) FROM (" +
                "SELECT first_name,invoice_number FROM patient_table AS P " +
                "LEFT JOIN invoice_track_table AS I ON P.id = I.patient_id " +

                "WHERE (:firstName IS NULL OR (first_name LIKE '%' || :firstName || '%') )" +
                " AND ( :lastName IS NULL OR (last_name LIKE '%' || :lastName || '%')  ) " +
                " AND (:startConsultDate IS NULL OR  (date(consultation_date_seconds,'unixepoch') BETWEEN :startConsultDate AND :endConsultDate)) " +
                " AND (:startAge  IS NULL OR  ( age BETWEEN :startAge AND :endAge)) " +
                " AND (:gender IS NULL OR  gender = :gender) " +
                " AND ( :diagnosis  IS NULL OR (diagnosis LIKE '%' || :diagnosis || '%') )" +
                " AND ( :completion  IS NULL OR (CASE WHEN :completion = 0 THEN invoice_number IS NULL ELSE invoice_number IS NOT NULL END) )" +
                ")"
    )
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
    ): Int


    @Query(
        "SELECT P.first_name,P.last_name,P.age,P.gender,P.phone_number,P.consultation_date_seconds,P.doctor_full_name,P.diagnosis,P.frequency,P.session_count,P.session_price,P.social_coverage,sum(S.amount_payed) AS totalAmountPayed, count(S.id) AS doneSessions " +
                " FROM patient_table AS P" +
                " LEFT JOIN session_table AS S on P.id = S.patient_id" +
                " where P.id = :patientId" +
                " group by P.id"
    )
    fun loadPatientDetailsWithId(patientId: Long): Flow<PatientDetails>

    @Query(
        "SELECT count(*) FROM invoice_track_table " +
                "WHERE patient_id = :patientId "
    )
    fun invoiceExistsForPatientWithId(patientId: Long): Flow<Int>

}