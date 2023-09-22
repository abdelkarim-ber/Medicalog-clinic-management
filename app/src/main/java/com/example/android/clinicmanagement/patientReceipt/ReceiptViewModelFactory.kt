package com.example.android.clinicmanagement.patientReceipt

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.InvoiceRepository
import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.data.repositories.QuotationRepository
import com.example.android.clinicmanagement.patientProfile.PatientProfileViewModel

class ReceiptViewModelFactory(
    private val patientId: Long,
    private val receiptType: ReceiptType,
    private val quotationRepository: QuotationRepository,
    private val invoiceRepository: InvoiceRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReceiptViewModel::class.java)) {
            return ReceiptViewModel(
                patientId,
                receiptType,
                quotationRepository, invoiceRepository,application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}