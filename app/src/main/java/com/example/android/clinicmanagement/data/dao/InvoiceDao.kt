package com.example.android.clinicmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.InvoiceTrack
import com.example.android.clinicmanagement.data.models.Invoice
import com.example.android.clinicmanagement.data.models.Receipt

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
        "SELECT invoice_number AS receiptNumber," +
                "I.date_in_seconds," +
                "P.first_name," +
                "P.last_name," +
                "P.doctor_full_name," +
                "P.diagnosis," +
                "P.frequency," +
                "count(S.id) AS session_count," +
                "P.session_price," +
                "sum(S.amount_payed) AS total " +
                "FROM invoice_track_table AS I " +
                "JOIN patient_table AS P ON I.patient_id = P.id " +
                "JOIN session_table AS S ON S.patient_id = P.id " +
                "WHERE I.patient_id = :patientId " +
                "GROUP BY P.id"
    )
    suspend fun getInvoiceWithPatientId(patientId: Long): Receipt?


    @Query(
        "SELECT count(*) FROM invoice_track_table " +
                "WHERE patient_id = :patientId "
    )
    suspend fun searchForPatientWithId(patientId: Long): Int

    suspend fun patientExistsWithId(patientId: Long) = searchForPatientWithId(patientId) == 1
}