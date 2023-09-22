package com.example.android.clinicmanagement.patientReceipt

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.clinicmanagement.data.models.Receipt
import com.example.android.clinicmanagement.data.repositories.InvoiceRepository
import com.example.android.clinicmanagement.data.repositories.QuotationRepository
import com.example.android.clinicmanagement.domain.GenerateReceiptUseCase
import com.example.android.clinicmanagement.domain.PrepareReceiptForPrintUseCase
import com.example.android.clinicmanagement.utilities.convertNumberToLetters
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReceiptViewModel(
    private val patientId:Long,
    private val receiptType: ReceiptType,
    private val quotationRepository: QuotationRepository,
    private val invoiceRepository: InvoiceRepository,
    application: Application,
    private val generateReceiptUseCase: GenerateReceiptUseCase = GenerateReceiptUseCase(),
) :AndroidViewModel(application) {


    /**
     * Variable for displaying receipt details.
     */
    private val _receiptDetails = MutableLiveData<Receipt?>()
    /**
     * The immutable and exposed version of [_receiptDetails].
     */
    val receiptDetails : LiveData<Receipt?> = _receiptDetails
    /**
     * Variable that tells whether to show the circular progress indicator or not.
     */
    private val _showCircularProgress:MutableLiveData<Boolean> = MutableLiveData()

    /**
     * the immutable and exposed version of [_showCircularProgress]
     */
    val showCircularProgress: LiveData<Boolean> = _showCircularProgress
    /**
     * Variable that tells the fragment to print the patient receipt .
     */
    private val _printPatientReceipt:MutableLiveData<Boolean> = MutableLiveData()

    /**
     * the immutable and exposed version of [_printPatientReceipt]
     */
    val printPatientReceipt: LiveData<Boolean> = _printPatientReceipt


    init {
        viewModelScope.launch {
            _receiptDetails.value = when(receiptType){
                ReceiptType.INVOICE -> invoiceRepository.getInvoiceWithPatientId(patientId)
                ReceiptType.QUOTATION ->  quotationRepository.getQuotationWithPatientId(patientId)
            }
        }

    }


    fun printReceipt() {
        viewModelScope.launch {
            _showCircularProgress.value = true
            delay(500L)
            _receiptDetails.value?.let { receipt ->
                generateReceiptUseCase(getApplication<Application?>().applicationContext,receipt,receiptType)
                _printPatientReceipt.value = true
            }
            _showCircularProgress.value = false

        }
    }

    /**
     * Called to clear the request of printing the patient's receipt.
     */
    fun receiptPrinted(){
        _printPatientReceipt.value = false
    }

}