package com.example.android.clinicmanagement.expenses

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository

class ExpensesViewModelFactory(private val expenditureRepository: ExpenditureRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesViewModel::class.java)) {
            return ExpensesViewModel(expenditureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}