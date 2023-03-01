package com.example.android.clinicmanagement.patientProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.patientsList.PatientsViewModel

class PatientProfileViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientProfileViewModel::class.java)) {
            return PatientProfileViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}