package com.example.android.clinicmanagement.patientProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.patientsList.PatientsViewModel

class PatientProfileViewModelFactory(private val patientId:Long, private val patientRepository: PatientRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientProfileViewModel::class.java)) {
            return PatientProfileViewModel(patientId, patientRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}