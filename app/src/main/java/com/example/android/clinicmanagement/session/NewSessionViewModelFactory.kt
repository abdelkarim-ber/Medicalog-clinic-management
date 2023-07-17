package com.example.android.clinicmanagement.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.data.repositories.SessionsRepository
import com.example.android.clinicmanagement.patientsList.PatientsViewModel

class NewSessionViewModelFactory( private val patientId:Long,private val sessionsRepository: SessionsRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewSessionViewModel::class.java)) {
            return NewSessionViewModel(patientId,sessionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}