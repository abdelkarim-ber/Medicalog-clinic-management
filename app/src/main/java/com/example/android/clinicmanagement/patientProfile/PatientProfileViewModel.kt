package com.example.android.clinicmanagement.patientProfile


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.patientReceipt.ReceiptType


class PatientProfileViewModel(
    patientId: Long, patientRepository: PatientRepository
) : ViewModel() {

    companion object {
        const val MIN_PATIENT_DONE_SESSIONS = 1
    }
    /**
     * Variable that tells the fragment to navigate to [PatientHistoryFragment]
     * It takes the patient Id as a value.
     */
    private val _navigateToPatientHistory = MutableLiveData<Long?>()

    /**
     * Variable that tells the fragment to navigate to [NewSessionFragment]
     * It takes the patient Id as a Long value.
     */
    private val _navigateToAddNewSession = MutableLiveData<Long?>()

    /**
     * Variable that tells the fragment to navigate to [ReceiptFragment]
     * It takes a pair of Int that represent type of receipt to generate
     * whether it's a quotation or an invoice, and the patient Id as Long value.
     */
    private val _navigateToReceipt = MutableLiveData<Pair<Int, Long>?>()

    /**
     * Variable that tells the fragment to navigate to [PatientFormFragment].
     * taking the patient Id as Long value
     */
    private val _navigateToPatientInfoUpdate = MutableLiveData<Long?>()

    /**
     * The immutable and exposed version of [_navigateToPatientHistory].
     */
    val navigateToPatientHistory: LiveData<Long?>
        get() = _navigateToPatientHistory

    /**
     * The immutable and exposed version of [_navigateToAddNewSession].
     */
    val navigateToAddNewSession: LiveData<Long?>
        get() = _navigateToAddNewSession

    /**
     * The immutable and exposed version of [_navigateToReceipt].
     */
    val navigateToReceipt: LiveData<Pair<Int, Long>?>
        get() = _navigateToReceipt

    /**
     * The immutable and exposed version of [_navigateToPatientInfoUpdate].
     */
    val navigateToPatientInfoUpdate: LiveData<Long?>
        get() = _navigateToPatientInfoUpdate


    /**
     * Variable for displaying patient details after loading them from the database.
     */
    val patientDetails = patientRepository.loadPatientDetailsWithId(patientId).asLiveData()

    /**
     * Variable for whether to display or not the invoice button according to
     * the patient done sessions number.
     */
    val invoiceAvailable = Transformations.map(patientDetails){
        it.doneSessions >= MIN_PATIENT_DONE_SESSIONS
    }

    /**
     * Variable for preventing the user to add session for the current patient if
     * his invoice data exists in the database.
     */
    val invoiceGenerated = patientRepository.invoiceExistsForPatientWithId(patientId).asLiveData()


    /**
     * Called when we click patient history button.
     */
    fun onPatientHistoryClicked(id: Long) {
        _navigateToPatientHistory.value = id
    }

    /**
     * Called after the patient history button is clicked
     * to clear the navigation request.
     */
    fun onPatientHistoryNavigated() {
        _navigateToPatientHistory.value = null
    }

    /**
     * Called when we click patient info update button.
     */
    fun onPatientInfoUpdateClicked(id: Long) {
        _navigateToPatientInfoUpdate.value = id
    }

    /**
     * Called after the patient info update button is clicked
     * to clear the navigation request.
     */
    fun onPatientInfoUpdateNavigated() {
        _navigateToPatientInfoUpdate.value = null
    }

    /**
     * Called when we click Invoice button.
     */
    fun onReceiptInvoiceClicked(id: Long) {
        _navigateToReceipt.value = ReceiptType.INVOICE.number to id
    }

    /**
     * Called when we click Quotation button.
     */
    fun onReceiptQuotationClicked(id: Long) {
        _navigateToReceipt.value = ReceiptType.QUOTATION.number to id
    }

    /**
     * Called after Invoice or Quotation button is clicked
     * to clear the navigation request.
     */
    fun onReceiptNavigated() {
        _navigateToReceipt.value = null
    }

    /**
     * Called when we click Add new session button.
     */
    fun onAddNewSessionClicked(id: Long) {
        _navigateToAddNewSession.value = id
    }

    /**
     * Called after Add new session button is clicked
     * to clear the navigation request.
     */
    fun onAddNewSessionNavigated() {
        _navigateToAddNewSession.value = null
    }


}