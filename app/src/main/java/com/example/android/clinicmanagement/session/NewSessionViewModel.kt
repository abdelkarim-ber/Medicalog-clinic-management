package com.example.android.clinicmanagement.session

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.repositories.SessionsRepository
import com.example.android.clinicmanagement.domain.ValidateTextUseCase
import com.example.android.clinicmanagement.utilities.convertDateTimeStringToDateSeconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewSessionViewModel
    (
    private val patientId:Long,
    private val sessionsRepository: SessionsRepository,
    private val validateTextUseCase: ValidateTextUseCase = ValidateTextUseCase()

) : ViewModel() {

    /**
     * Variable that tells whether to show the circular progress indicator or not.
     */
    private val _showCircularProgress:MutableLiveData<Boolean> = MutableLiveData()

    /**
     * the immutable and exposed version of [_showCircularProgress]
     */
    val showCircularProgress: LiveData<Boolean> = _showCircularProgress

    /**
     * Variable that store the values of the textInputEditTexts
     * and the states of the error values of those textInputEditTexts.
     */
    var newSessionDataState = NewSessionDataState()

    /**
     * Called whenever we made a change in the textInputEditTexts
     * to save the changes each time in [newSessionDataState].
     */
    fun onEvent(event: NewSessionRegistrationEvent) {
        newSessionDataState = when (event) {
            is NewSessionRegistrationEvent.DateChanged -> newSessionDataState.copy(date = event.date)
            is NewSessionRegistrationEvent.TimeChanged -> newSessionDataState.copy(time = event.time)
            is NewSessionRegistrationEvent.AmountPayedChanged -> newSessionDataState.copy(
                amountPayed = event.amountPayed
            )
        }
    }

    /**
     * Called when we click the save button to persist the entered data after
     * we check that it is not empty.
     */
    fun submit() {
        viewModelScope.launch {

            //amount payed is an optional field that's why we don't verify if it's empty
            val dateResult = validateTextUseCase(newSessionDataState.date)
            val timeResult = validateTextUseCase(newSessionDataState.time)
            //After we verify if the entered data is valid or not (in this case, if it is blank or not),
            //we check if there is any error in those input fields.
            val hasError = listOf(dateResult, timeResult).any { !it.isSuccessful }

            if (hasError) {
                newSessionDataState.errorDate?.value = dateResult.errorMessage
                newSessionDataState.errorTime?.value = timeResult.errorMessage
            } else {
                // To show the circular progress indicator
                _showCircularProgress.value = true

                delay(700L)

               with(newSessionDataState) {
                   val  session = Session(
                        dateInSeconds = convertDateTimeStringToDateSeconds(
                            date,
                            time
                        ),
                        amountPayed = amountPayed.ifBlank { "0" }.toInt(),
                        patientId = patientId
                    )
                   sessionsRepository.addSession(session)
                }
                // To hide the circular progress indicator
                _showCircularProgress.value = false
            }
        }

    }
}