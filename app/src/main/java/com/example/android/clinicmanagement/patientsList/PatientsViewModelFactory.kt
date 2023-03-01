package com.example.android.clinicmanagement.patientsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PatientsViewModelFactory : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientsViewModel::class.java)) {
            return PatientsViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}