package com.example.android.clinicmanagement.data.repositories

import com.example.android.clinicmanagement.data.datasources.InvoiceDataSource
import com.example.android.clinicmanagement.data.models.Invoice
import com.example.android.clinicmanagement.data.models.InvoiceTrack
import com.example.android.clinicmanagement.data.models.Quotation
import com.example.android.clinicmanagement.data.models.QuotationTrack

class InvoiceRepository(private val invoiceDataSource: InvoiceDataSource) {

    suspend fun getInvoiceWithPatientId(patientId: Long): Invoice? {
        with(invoiceDataSource) {
            if (!patientExistsWithId(patientId)) {
                val invoiceTrack = InvoiceTrack(
                    invNumber = generateInvoiceTrackNumberForCurrentYear(),
                    patientId = patientId
                )
                addInvoiceTrack(invoiceTrack)
            }
            return getInvoiceWithPatientId(patientId)
        }
    }
    suspend fun deleteInvoiceTrack(invoiceTrack: InvoiceTrack) {
        invoiceDataSource.deleteInvoiceTrack(invoiceTrack)
    }
}