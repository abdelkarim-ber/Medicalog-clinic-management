package com.example.android.clinicmanagement.data.repositories

import android.util.Log
import com.example.android.clinicmanagement.data.datasources.QuotationDataSource
import com.example.android.clinicmanagement.data.models.Quotation
import com.example.android.clinicmanagement.data.models.QuotationTrack
import com.example.android.clinicmanagement.data.models.Receipt

class QuotationRepository(private val quotationDataSource: QuotationDataSource) {

    suspend fun getQuotationWithPatientId(patientId: Long): Receipt? {
        with(quotationDataSource) {
            if (!patientExistsWithId(patientId)) {
                val quotationTrack = QuotationTrack(
                    quoteNumber = generateQuotationTrackNumberForCurrentYear(),
                    patientId = patientId
                )
                addQuotationTrack(quotationTrack)
            }
            return getQuotationWithPatientId(patientId)
        }
    }
    suspend fun deleteQuotationTrack(quotationTrack:QuotationTrack) {
        quotationDataSource.deleteQuotationTrack(quotationTrack)
    }
}