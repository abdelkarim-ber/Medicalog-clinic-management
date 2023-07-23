package com.example.android.clinicmanagement.patientHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.SessionsRepository

class PatientHistoryViewModelFactory (private val patientId:Long,private val sessionsRepository: SessionsRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientHistoryViewModel::class.java)) {
            return PatientHistoryViewModel(patientId,sessionsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}