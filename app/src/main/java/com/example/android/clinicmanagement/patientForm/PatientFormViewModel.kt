package com.example.android.clinicmanagement.patientForm

import android.util.Log
import androidx.lifecycle.*
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.domain.*
import com.example.android.clinicmanagement.utilities.UiState
import com.github.mikephil.charting.utils.Utils.init
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PatientFormViewModel(
    private val patientId: Long,
    private val submitPatientInfoUseCase: SubmitPatientInfoUseCase,
    private val loadPatientInfoUseCase: LoadPatientInfoUseCase,
    private val validateTextUseCase: ValidateTextUseCase = ValidateTextUseCase(),
    private val validateFullNameUseCase: ValidateFullNameUseCase = ValidateFullNameUseCase(),
    private val validateAgeUseCase: ValidateAgeUseCase = ValidateAgeUseCase()
) : ViewModel() {

    var patientFormDataState = PatientFormDataState()

    private val _patientFormUIState: MutableLiveData<UiState> =
        MutableLiveData(UiState.Success(null))
    val patientFormUIState: LiveData<UiState> = _patientFormUIState

    private val _showSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean> = _showSnackBarEvent

    private val _clearFormEvent = MutableLiveData<Boolean>()
    val clearFormEvent: LiveData<Boolean> = _clearFormEvent


    init {
        if (patientId != -1L) {
            viewModelScope.launch {
                _patientFormUIState.value = UiState.Loading(R.string.form_loading_patient_info)
                delay(1000L)
                patientFormDataState = loadPatientInfoUseCase(patientId, patientFormDataState)
                _patientFormUIState.value = UiState.Success(patientFormDataState)
                //to keep the fields text changes after a configuration change
                delay(2000L)
                _patientFormUIState.value = UiState.Success(null)
            }
        }

    }

    fun onEvent(event: PatientRegistrationEvent) {
        when (event) {
            is PatientRegistrationEvent.FirstNameChanged -> patientFormDataState =
                patientFormDataState.copy(firstName = event.firstName)
            is PatientRegistrationEvent.LastNameChanged -> patientFormDataState =
                patientFormDataState.copy(lastName = event.lastName)
            is PatientRegistrationEvent.AgeChanged -> patientFormDataState =
                patientFormDataState.copy(age = event.age)
            is PatientRegistrationEvent.GenderChanged -> patientFormDataState =
                patientFormDataState.copy(gender = event.gender)
            is PatientRegistrationEvent.PhoneNumberChanged -> patientFormDataState =
                patientFormDataState.copy(phoneNumber = event.phoneNumber)
            is PatientRegistrationEvent.ConsultationDateChanged -> patientFormDataState =
                patientFormDataState.copy(consultationDate = event.consultationDate)
            is PatientRegistrationEvent.DoctorFullNameChanged -> patientFormDataState =
                patientFormDataState.copy(doctorFullName = event.doctorFullName)
            is PatientRegistrationEvent.DiagnosisChanged -> patientFormDataState =
                patientFormDataState.copy(diagnosis = event.diagnosis)
            is PatientRegistrationEvent.FrequencyChanged -> patientFormDataState =
                patientFormDataState.copy(frequency = event.frequency)
            is PatientRegistrationEvent.SessionCountChanged -> patientFormDataState =
                patientFormDataState.copy(sessionCount = event.sessionCount)
            is PatientRegistrationEvent.SessionPriceChanged -> patientFormDataState =
                patientFormDataState.copy(sessionPrice = event.sessionPrice)
            is PatientRegistrationEvent.SocialCoverageChanged -> patientFormDataState =
                patientFormDataState.copy(socialCoverage = event.socialCoverage)

        }
    }

    fun submit() {
        viewModelScope.launch {
            _patientFormUIState.value = UiState.Loading(R.string.form_validating_patient_info)
            delay(800L)
            val firstNameResult = validateFullNameUseCase(
                patientFormDataState.firstName
            )
            val lastNameResult = validateFullNameUseCase(
                patientFormDataState.lastName
            )
            val doctorFullNameResult = validateFullNameUseCase(
                patientFormDataState.doctorFullName
            )
            val ageResult = validateAgeUseCase(
                patientFormDataState.age
            )
            val genderResult = if (patientFormDataState.gender == -1) {
                ValidationResult(
                    isSuccessful = false,
                    errorMessage = R.string.form_error_field_required
                )
            } else
                ValidationResult(isSuccessful = true)

            val phoneNumberResult = validateTextUseCase(
                patientFormDataState.phoneNumber
            )
            val consultationDateResult = validateTextUseCase(
                patientFormDataState.consultationDate
            )
            val diagnosisResult = validateTextUseCase(
                patientFormDataState.diagnosis
            )
            val frequencyResult = validateTextUseCase(
                patientFormDataState.frequency
            )
            val sessionCountResult = validateTextUseCase(
                patientFormDataState.sessionCount
            )
            val sessionPriceResult = validateTextUseCase(
                patientFormDataState.sessionPrice
            )

            val hasError = listOf(
                firstNameResult,
                lastNameResult,
                doctorFullNameResult,
                ageResult,
                genderResult,
                phoneNumberResult,
                consultationDateResult,
                diagnosisResult,
                frequencyResult,
                sessionCountResult,
                sessionPriceResult
            ).any { !it.isSuccessful }
            if (hasError) {
                //to let the forms be visible to show the errors
                _patientFormUIState.value = UiState.Success(null)

                with(patientFormDataState) {
                    errorFirstName?.value = firstNameResult.errorMessage
                    errorLastName?.value = lastNameResult.errorMessage
                    errorAge?.value = ageResult.errorMessage
                    errorGender?.value = genderResult.errorMessage
                    errorPhoneNumber?.value = phoneNumberResult.errorMessage
                    errorConsultationDate?.value = consultationDateResult.errorMessage
                    errorDoctorFullName?.value = doctorFullNameResult.errorMessage
                    errorDiagnosis?.value = diagnosisResult.errorMessage
                    errorFrequency?.value = frequencyResult.errorMessage
                    errorSessionCount?.value = sessionCountResult.errorMessage
                    errorSessionPrice?.value = sessionPriceResult.errorMessage
                }
            } else {
                //Remove the error indicators if there are any.
                with(patientFormDataState) {
                    errorFirstName?.value = null
                    errorLastName?.value = null
                    errorAge?.value = null
                    errorGender?.value = null
                    errorPhoneNumber?.value = null
                    errorConsultationDate?.value = null
                    errorDoctorFullName?.value = null
                    errorDiagnosis?.value = null
                    errorFrequency?.value = null
                    errorSessionCount?.value = null
                    errorSessionPrice?.value = null
                }

                _patientFormUIState.value = UiState.Loading(R.string.form_persisting_patient_info)
                delay(800L)
                //add new patient or update an existing patient info in case of patientId != -1L
                submitPatientInfoUseCase(patientFormDataState, patientId)
                _patientFormUIState.value = UiState.Success(null)
                //Show the snackBar
                _showSnackBarEvent.value = true
                delay(800L)
                //After a delay we clear all the fields
                clearFormFields()
            }

        }


    }

    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }

    fun formFieldsCleared() {
        _clearFormEvent.value = false
    }

    private fun clearFormFields() {
        patientFormDataState = patientFormDataState.copy(gender = -1)
        _clearFormEvent.value = true
    }

}
