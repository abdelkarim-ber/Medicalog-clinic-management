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


    /**
     * Variable to set the current state of the screen to either show a loading animation
     * in the case of patient info update or show the actual form.
     */
    private val _patientFormUIState: MutableLiveData<UiState> =
        MutableLiveData(UiState.Success)
    /**
     * The immutable and exposed version of [_patientFormUIState].
     */
    val patientFormUIState: LiveData<UiState> = _patientFormUIState
    /**
     * Variable that tells the fragment to show the snack bar.
     */
    private val _showSnackBarEvent = MutableLiveData<Boolean>()
    /**
     * The immutable and exposed version of [_showSnackBarEvent].
     */
    val showSnackBarEvent: LiveData<Boolean> = _showSnackBarEvent
    /**
     * Variable that tells the fragment to show a toast message in case of an input error.
     */
    private val _showToastEvent = MutableLiveData<Boolean>()
    /**
     * The immutable and exposed version of [_showToastEvent].
     */
    val showToastEvent: LiveData<Boolean> = _showToastEvent

    /**
     * Variable that tells the fragment to clear all input fields.
     */
    private val _clearFormEvent = MutableLiveData<Boolean>()
    /**
     * The immutable and exposed version of [_clearFormEvent].
     */
    val clearFormEvent: LiveData<Boolean> = _clearFormEvent

    /**
     * Variable where we store the user info to display
     * in case of a patient info update.
     */
    private val _oldUserInfo= MutableLiveData<PatientFormDataState?>()

    /**
     * The immutable and exposed version of [_oldUserInfo].
     */
    val oldUserInfo : LiveData<PatientFormDataState?> = _oldUserInfo

    /**
     * Variable that store the values of the input fields
     * and the states of their error values.
     */
    var patientFormDataState = PatientFormDataState()

    init {
        if (patientId != -1L) {
            viewModelScope.launch {
                _patientFormUIState.value = UiState.Loading(R.string.form_loading_patient_info)
                patientFormDataState = loadPatientInfoUseCase(patientId, patientFormDataState)
                _oldUserInfo.value = patientFormDataState
                //Delay is for giving enough time for values to be set into their corresponding fields.
                delay(2000L)
                //To not re-assign previous values after a configuration change
                _oldUserInfo.value = null
                _patientFormUIState.value = UiState.Success
            }
        }

    }
    /**
     * Called whenever we made a change in the input fields
     * to save the changes in [patientFormDataState].
     */
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
    /**
     * Called when we click the save button to persist the entered data after
     * we check that it is valid.
     */
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
                _patientFormUIState.value = UiState.Success

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
                //To show a toast message to the user indicating that there is an error.
                _showToastEvent.value = true

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
                _patientFormUIState.value = UiState.Success
                //Show the snackBar
                _showSnackBarEvent.value = true
                delay(800L)
                //After a delay we clear all the fields
                clearFormFields()
            }
        }
    }

    /**
     * Called to clear the request of showing the snack bar.
     */
    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }
    /**
     * Called to clear the request of showing the toast message.
     */
    fun doneShowingToastMessage() {
        _showToastEvent.value = false
    }
    /**
     * Called to clear the request of clearing the input fields.
     */
    fun formFieldsCleared() {
        _clearFormEvent.value = false
    }

    /**
     * Called to clear the input fields.
     */
    private fun clearFormFields() {
        patientFormDataState = patientFormDataState.copy(gender = -1)
        _clearFormEvent.value = true
    }

}
