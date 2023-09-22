package com.example.android.clinicmanagement.data.repositories

import android.util.Log
import com.example.android.clinicmanagement.data.datasources.InvoiceDataSource
import com.example.android.clinicmanagement.data.models.*

class InvoiceRepository(private val invoiceDataSource: InvoiceDataSource) {

    suspend fun getInvoiceWithPatientId(patientId: Long): Receipt? {
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