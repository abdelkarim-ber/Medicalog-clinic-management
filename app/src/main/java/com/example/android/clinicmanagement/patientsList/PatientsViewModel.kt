package com.example.android.clinicmanagement.patientsList

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PatientsViewModel : ViewModel() {


    private val _navigateToPatientProfile = MutableLiveData<Pair<View,Long>?>()

    val navigateToPatientProfile: LiveData<Pair<View,Long>?>
        get() = _navigateToPatientProfile

    fun onPatientClicked(view: View,id: Long) {
        _navigateToPatientProfile.value = view to id
    }

    fun onPatientProfileNavigated() {
        _navigateToPatientProfile.value = null
    }

}