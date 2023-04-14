package com.example.android.clinicmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.Invoice

@Dao
interface InvoiceDao {
    @Insert
    suspend fun insert(invoice: Invoice)

    @Delete
    suspend fun delete(invoice: Invoice)

    @Query("SELECT * FROM invoice_table "+
            "WHERE patient_id = :patientId "
    )
    suspend fun getInvoiceWithPatientId(patientId:Int): Invoice?

    @Query("SELECT max(invoice_number)+1 FROM invoice_table "+
            "WHERE strftime('%Y', date_in_seconds, 'unixepoch') = strftime('%Y', 'now')"
    )
    suspend fun generateInvoiceNumber():Int?

    @Query("SELECT count(*) > 0 FROM invoice_table "+
            "WHERE patient_id = :patientId "
    )
    suspend fun searchForPatientWithId(patientId:Int):Int

    suspend fun patientExistsWithId(patientId:Int)= searchForPatientWithId(patientId) == 1
}