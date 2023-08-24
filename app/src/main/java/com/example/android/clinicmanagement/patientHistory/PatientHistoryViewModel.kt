package com.example.android.clinicmanagement.patientHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.repositories.SessionsRepository
import com.example.android.clinicmanagement.utilities.UiState
import kotlinx.coroutines.launch

class PatientHistoryViewModel(patientId: Long, private val sessionsRepository: SessionsRepository) :
    ViewModel() {

    /**
     * Variable to set the current state of the screen to either show the actual content
     * or show a failure message when there is no content to show.
     */
    private val _patientHistoryUIState: MutableLiveData<UiState> = MutableLiveData()

    /**
     * The immutable and exposed version of [_patientHistoryUIState].
     */
    val patientHistoryUIState: LiveData<UiState> = _patientHistoryUIState

    /**
     * Variable to load the patient sessions history from the database.
     */
    val patientSessionsHistoryList = sessionsRepository.loadSessionsWithPatientID(patientId)


    /**
     * Variable that tells the fragment to show the snack bar.
     */
    private val _showSnackBarEvent = MutableLiveData<Boolean>()

    /**
     * The immutable and exposed version of [_showSnackBarEvent].
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent


    /**
     * Called each time we click on the delete icon of sessions history item
     * to delete the desired session.
     */
    fun deleteSession(session: Session) {
        viewModelScope.launch {
            sessionsRepository.deleteSession(session)
            showSnackBar()
        }
    }

    /**
     * Called after we delete the desired session to
     * confirm that the deletion was done successfully.
     */
    private fun showSnackBar() {
        _showSnackBarEvent.value = true
    }
    /**
     * Called after [showSnackBar] is called to clear
     * the request of showing the snack bar.
     */
    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }


    /**
     * Called when we have no data to show to the user
     * to display the place holder screen.
     */
    fun showEmptyScreen() {
        _patientHistoryUIState.value = UiState.Failure(
            R.string.patient_session_empty_state_tagline,
            R.string.patient_session_empty_state_message,
            R.drawable.img_placeholder_empty_data
        )
    }

    /**
     * Called when we actually have some data to show to the user.
     */
    fun showContent() {
        _patientHistoryUIState.value = UiState.Success
    }

}