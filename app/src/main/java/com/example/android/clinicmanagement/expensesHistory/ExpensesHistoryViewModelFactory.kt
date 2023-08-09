package com.example.android.clinicmanagement.expensesHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.expenses.ExpensesViewModel

class ExpensesHistoryViewModelFactory(private val expenditureRepository: ExpenditureRepository) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ExpensesHistoryViewModel::class.java)) {
            return ExpensesHistoryViewModel(expenditureRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}