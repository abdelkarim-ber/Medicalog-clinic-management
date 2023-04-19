package com.example.android.clinicmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.Quotation
import com.example.android.clinicmanagement.data.models.QuotationTrack

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
        "SELECT date_in_seconds,quotation_number,first_name,last_name, (session_count*session_price) AS total, session_count " +
                "FROM quotation_track_table AS Q " +
                "JOIN patient_table AS P ON Q.patient_id = P.id " +
                "WHERE Q.patient_id = :patientId "
    )
    suspend fun getQuotationWithPatientId(patientId: Long): Quotation

    @Query(
        "SELECT count(*) > 0 FROM quotation_track_table " +
                "WHERE patient_id = :patientId "
    )
    suspend fun searchForPatientWithId(patientId: Long): Int

    suspend fun patientExistsWithId(patientId: Long) = searchForPatientWithId(patientId) == 1


}