package com.example.android.clinicmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.Quotation
import com.example.android.clinicmanagement.data.models.QuotationTrack
import com.example.android.clinicmanagement.data.models.Receipt

@Dao
interface QuotationDao {

    @Insert
    suspend fun insert(quotationTrack: QuotationTrack)

    @Delete
    suspend fun delete(quotationTrack: QuotationTrack)

    @Query(
        "SELECT max(quotation_number)+1 FROM quotation_track_table " +
                "WHERE strftime('%Y', date_in_seconds, 'unixepoch') = strftime('%Y', 'now')"
    )
    suspend fun generateQuotationTrackNumber(): Int?

    suspend fun generateQuotationTrackNumberForCurrentYear() = generateQuotationTrackNumber() ?: 1

    @Query(
        "SELECT quotation_number AS receiptNumber," +
                " Q.date_in_seconds," +
                " P.first_name," +
                " P.last_name," +
                " P.doctor_full_name," +
                "P.diagnosis," +
                "P.frequency," +
                "P.session_count," +
                "P.session_price," +
                " (session_count*session_price) AS total " +
                "FROM quotation_track_table AS Q " +
                "JOIN patient_table AS P ON Q.patient_id = P.id " +
                "WHERE Q.patient_id = :patientId "
    )
    suspend fun getQuotationWithPatientId(patientId: Long): Receipt?

    @Query(
        "SELECT count(*) FROM quotation_track_table " +
                "WHERE patient_id = :patientId "
    )
    suspend fun searchForPatientWithId(patientId: Long): Int

    suspend fun patientExistsWithId(patientId: Long) = searchForPatientWithId(patientId) == 1


}