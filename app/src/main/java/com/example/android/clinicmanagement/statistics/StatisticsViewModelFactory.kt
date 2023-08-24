package com.example.android.clinicmanagement.statistics

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.data.repositories.SessionsRepository

class StatisticsViewModelFactory(private val sessionsRepository: SessionsRepository, private val expenditureRepository: ExpenditureRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)) {
            return StatisticsViewModel(sessionsRepository,expenditureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}