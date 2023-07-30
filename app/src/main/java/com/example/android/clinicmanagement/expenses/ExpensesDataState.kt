package com.example.android.clinicmanagement.expenses

import androidx.lifecycle.MutableLiveData

data class ExpensesDataState(
    val expensesNumber: Int = -1,
    val errorExpensesNumber: MutableLiveData<Boolean>? = MutableLiveData(false),
    val amountPayed: String = "",
    val errorAmountPayed: MutableLiveData<Int?>? = MutableLiveData(null)
)
