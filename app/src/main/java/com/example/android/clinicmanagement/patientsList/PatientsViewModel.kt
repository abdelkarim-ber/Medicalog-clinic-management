package com.example.android.clinicmanagement.patientsList

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.PatientStatus
import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.utilities.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class PatientsViewModel(private val patientRepository: PatientRepository) : ViewModel() {

    /**
     * Variable to set the current state of the screen to either show a loading animation
     * or show the actual content or show a failure message when there is no content to show.
     */
    private val _patientsUIState: MutableLiveData<UiState> =
        MutableLiveData(UiState.Loading(R.string.patients_list_loading_message))
    /**
     * The immutable and exposed version of [_patientsUIState].
     */
    val patientsUIState: LiveData<UiState> = _patientsUIState


    /**
     * Variable that tells the fragment to navigate to [PatientFormFragment].
     * [View?] here represents the fab button view to helps us animate
     * the Material container transformation.
     */
    private val _navigateToNewPatientEvent = MutableLiveData<Pair<View?, Boolean>>()
    /**
     * The immutable and exposed version of [_navigateToNewPatientEvent].
     */
    val navigateToNewPatientEvent: LiveData<Pair<View?, Boolean>>
        get() = _navigateToNewPatientEvent


    /**
     * Variable that tells the fragment to navigate to a specific [PatientProfileFragment].
     * [View] here represents the fab button view to helps us animate
     * the Material container transformation.
     */
    private val _navigateToPatientProfileEvent = MutableLiveData<Pair<View, Long>?>()
    /**
     * The immutable and exposed version of [_navigateToPatientProfileEvent].
     */
    val navigateToPatientProfileEvent: LiveData<Pair<View, Long>?>
        get() = _navigateToPatientProfileEvent


    /**
     * Variable that tells the adapter to collect from a new pager
     * in case when we have a new set of filter informations.
     */
    private val _newPatientsListPagerEvent = MutableLiveData<Flow<PagingData<PatientStatus>>>()
    /**
     * The immutable and exposed version of [_newPatientsListPagerEvent].
     */
    val newPatientsListPagerEvent: LiveData<Flow<PagingData<PatientStatus>>> = _newPatientsListPagerEvent

    /**
     * Variable to set the count of results after a filter is applied.
     */
    private val _patientsCount = MutableLiveData<Int?>()
    /**
     * The immutable and exposed version of [_patientsCount].
     */
    val patientsCount: LiveData<Int?>
        get() = _patientsCount

    /**
     * Variable to store the job of the coroutine responsible for loading records
     * that matches the current filter, which also helps us canceling the previous
     * jobs each time we call [loadPatientsCount] function.
     */
    private var countingProcessJob: Job? = null

    /**
     * Variable that represents the current set of filter informations
     * which we change every time we call [onEvent].
     */
    private var filterDataState = FilterDataState()

    /**
     * Variable where we store the current [filterDataState]
     * to help us know if a change is made when we call [loadPatients]
     * to avoid reloading the same data.
     */
    private var lastFilterDataState: FilterDataState? = null

    init {
        loadPatientsCount()
        loadPatients()
    }

    /**
     * Called whenever an input change is made on a filter widgets.
     */
    fun onEvent(event: FilterCriteriaChangeEvent) {
        when (event) {
            is FilterCriteriaChangeEvent.FirstNameChanged -> filterDataState =
                filterDataState.copy(firstName = event.firstName)
            is FilterCriteriaChangeEvent.LastNameChanged -> filterDataState =
                filterDataState.copy(lastName = event.lastName)
            is FilterCriteriaChangeEvent.AgeChanged -> filterDataState =
                filterDataState.copy(ageRangeStart = event.ageStart, ageRangeEnd = event.ageEnd)
            is FilterCriteriaChangeEvent.GenderChanged -> filterDataState =
                filterDataState.copy(gender = event.gender)
            is FilterCriteriaChangeEvent.ConsultationDateChanged -> filterDataState =
                filterDataState.copy(
                    consultDateRangeStart = event.consultationDateStart,
                    consultDateRangeEnd = event.consultationDateEnd
                )
            is FilterCriteriaChangeEvent.DiagnosisChanged -> filterDataState =
                filterDataState.copy(diagnosis = event.diagnosis)
            is FilterCriteriaChangeEvent.SessionsCompletionChanged -> filterDataState =
                filterDataState.copy(sessionsCompletionState = event.sessionsCompletionState)
        }

        loadPatientsCount()
    }

    /**
     * Called whenever we are in the process of loading data
     * so we can show the user a loading screen.
     */
    fun showLoadingScreen() {
        _patientsUIState.value = UiState.Loading(R.string.patients_list_loading_message)
    }
    /**
     * Called after we finish the process of loading data
     * to hide the loading screen and show the content if
     * there is one.
     */
    fun hideLoadingScreen() {
        // Check if the current state is loading state
        // to avoid hiding the failure screen if the current
        // UI state is the failure state.
        if (_patientsUIState.value is UiState.Loading) {
            _patientsUIState.value = UiState.Success
        }
    }

    /**
     * Called when the fab button is clicked
     */
    fun onAddNewPatientClicked(view: View) {
        _navigateToNewPatientEvent.value = view to true
    }
    /**
     * Called after the fab button is clicked
     * to clear the navigation request.
     */
    fun onNewPatientNavigated() {
        _navigateToNewPatientEvent.value = null to false
    }
    /**
    * Called when a patients list item is clicked
     * passing the clicked view and the item id
     * to navigate to a specific [PatientProfileFragment]
    */
    fun onPatientClicked(view: View, id: Long) {
        _navigateToPatientProfileEvent.value = view to id
    }
    /**
     * Called after the patients list item is clicked
     */
    fun onPatientProfileNavigated() {
        _navigateToPatientProfileEvent.value = null
    }

    /**
     * Load patients count to then display as a title
     * for the front layer of the backdrop
     */
    private fun loadPatientsCount() {
        //canceling previous countingProcessJob if exists
        countingProcessJob?.cancel()

        countingProcessJob = viewModelScope.launch {
            //patients count value null means that the results count is processing in the background
            _patientsCount.value = null
            delay(1000L)
            //Here we check if there is any criteria to search for otherwise we just set -1 as count value
            if (filterDataState != FilterDataState()) {
                _patientsCount.value =
                    patientRepository.countPatientsStatusWithFilter(filterDataState)
            } else {
                _patientsCount.value = -1
            }

        }
    }

    /**
     * Load the patients list if the count returned from [loadPatientsCount] is non 0.
     */
    fun loadPatients() {
        //Here we check if the filterDataState is the same from
        //the last call of loadPatients() function if it is
        //we just return immediately.
        if (lastFilterDataState == filterDataState) {
            return
        }

        viewModelScope.launch {
            countingProcessJob?.join()
            //Here if the patientsCount is zero we just show an empty state screen.
            if (_patientsCount.value == 0) {
                _patientsUIState.value = UiState.Failure(
                    R.string.patients_list_empty_state_tagline,
                    R.string.patients_list_empty_state_message,
                    R.drawable.img_placeholder_no_results
                )
                //Otherwise we load the actual Patients list data
            } else {
                _newPatientsListPagerEvent.value =
                    patientRepository.loadPatientsStatusWithFilter(filterDataState)
                        .cachedIn(viewModelScope)
            }
            lastFilterDataState = filterDataState
        }
    }

}