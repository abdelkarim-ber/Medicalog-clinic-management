package com.example.android.clinicmanagement.data.repositories

import com.example.android.clinicmanagement.data.datasources.QuotationDataSource
import com.example.android.clinicmanagement.data.models.Quotation
import com.example.android.clinicmanagement.data.models.QuotationTrack

class QuotationRepository(private val quotationDataSource: QuotationDataSource) {

    suspend fun getQuotationWithPatientId(patientId: Long): Quotation {
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