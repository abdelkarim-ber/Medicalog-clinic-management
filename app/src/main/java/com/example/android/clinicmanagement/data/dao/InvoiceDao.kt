package com.example.android.clinicmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.InvoiceTrack
import com.example.android.clinicmanagement.data.models.Invoice

@Dao
interface InvoiceDao {
    @Insert
    suspend fun insert(invoiceTrack: InvoiceTrack)

    @Delete
    suspend fun delete(invoiceTrack: InvoiceTrack)


    @Query(
        "SELECT max(invoice_number)+1 FROM invoice_track_table " +
                "WHERE strftime('%Y', date_in_seconds, 'unixepoch') = strftime('%Y', 'now')"
    )
    suspend fun generateInvoiceTrackNumber(): Int?

    suspend fun generateInvoiceTrackNumberForCurrentYear() = generateInvoiceTrackNumber() ?: 1

    @Query(
        "SELECT I.date_in_seconds,I.invoice_number,P.first_name,P.last_name,sum(S.amount_payed) AS total,count(S.id) AS doneSessions " +
                "FROM invoice_track_table AS I " +
                "JOIN patient_table AS P ON I.patient_id = P.id " +
                "JOIN session_table AS S ON S.patient_id = P.id " +
                "WHERE I.patient_id = :patientId " +
                "GROUP BY P.first_name"
    )
    suspend fun getInvoiceWithPatientId(patientId: Long): Invoice?


    @Query(
        "SELECT count(*) > 0 FROM invoice_track_table " +
                "WHERE patient_id = :patientId "
    )
    suspend fun searchForPatientWithId(patientId: Long): Int

    suspend fun patientExistsWithId(patientId: Long) = searchForPatientWithId(patientId) == 1
}