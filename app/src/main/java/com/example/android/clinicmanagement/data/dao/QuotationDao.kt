package com.example.android.clinicmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.Quotation

@Dao
interface QuotationDao {

    @Insert
    suspend fun insert(quotation: Quotation)

    @Delete
    suspend fun delete(quotation: Quotation)

    @Query("SELECT * FROM quotation_table "+
            "WHERE patient_id = :patientId "
    )
    suspend fun getQuotationWithPatientId(patientId:Int): Quotation?

    @Query("SELECT max(quotation_number)+1 FROM quotation_table "+
            "WHERE strftime('%Y', date_in_seconds, 'unixepoch') = strftime('%Y', 'now')"
    )
    suspend fun generateQuotationNumber():Int?

    @Query("SELECT count(*) > 0 FROM quotation_table "+
            "WHERE patient_id = :patientId "
    )
    suspend fun searchForPatientWithId(patientId:Int):Int

    suspend fun patientExistsWithId(patientId:Int) = searchForPatientWithId(patientId) == 1


}