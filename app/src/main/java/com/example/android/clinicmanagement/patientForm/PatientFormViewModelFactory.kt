package com.example.android.clinicmanagement.patientForm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.domain.LoadPatientInfoUseCase
import com.example.android.clinicmanagement.domain.SubmitPatientInfoUseCase

import com.example.android.clinicmanagement.statistics.StatisticsViewModel

class PatientFormViewModelFactory(
    private val patientId: Long,
    private val submitPatientInfoUseCase: SubmitPatientInfoUseCase,
    private val loadPatientInfoUseCase: LoadPatientInfoUseCase
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PatientFormViewModel::class.java)) {
            return PatientFormViewModel(patientId,submitPatientInfoUseCase, loadPatientInfoUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}