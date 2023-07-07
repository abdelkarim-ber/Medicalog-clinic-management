package com.example.android.clinicmanagement.patientsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.PatientRepository

class PatientsViewModelFactory(private val patientRepository: PatientRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientsViewModel::class.java)) {
            return PatientsViewModel(patientRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}