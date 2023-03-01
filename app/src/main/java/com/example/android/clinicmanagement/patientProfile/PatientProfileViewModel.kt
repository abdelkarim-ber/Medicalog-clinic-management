package com.example.android.clinicmanagement.patientProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PatientProfileViewModel : ViewModel() {
companion object{
    const val QUOTATION = 1
    const val INVOICE = 2
}

    private val _navigateToPatientHistory = MutableLiveData<Long?>()

    val navigateToPatientHistory: LiveData<Long?>
        get() = _navigateToPatientHistory

    fun onPatientHistoryClicked(id: Long) {
        _navigateToPatientHistory.value = id
    }

    fun onPatientHistoryNavigated() {
        _navigateToPatientHistory.value = null
    }

    private val _navigateToPatientInfoUpdate = MutableLiveData<Long?>()
    val navigateToPatientInfoUpdate: LiveData<Long?>
        get() = _navigateToPatientInfoUpdate

    fun onPatientInfoUpdateClicked(id: Long) {
        _navigateToPatientInfoUpdate.value = id
    }

    fun onPatientInfoUpdateNavigated() {
        _navigateToPatientInfoUpdate.value = null
    }

    private val _navigateToReceipt = MutableLiveData<Pair<Int,Long>?>()
    val navigateToReceipt: LiveData<Pair<Int,Long>?>
        get() = _navigateToReceipt

    fun onReceiptInvoiceClicked(id: Long) {
        _navigateToReceipt.value = INVOICE to id
    }
    fun onReceiptQuotationClicked(id: Long) {
        _navigateToReceipt.value = QUOTATION to id
    }

    fun onReceiptNavigated() {
        _navigateToReceipt.value = null
    }

}