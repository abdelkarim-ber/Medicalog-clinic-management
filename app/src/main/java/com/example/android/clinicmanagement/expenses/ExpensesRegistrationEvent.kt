package com.example.android.clinicmanagement.expenses

import com.example.android.clinicmanagement.session.NewSessionRegistrationEvent

sealed class ExpensesRegistrationEvent {
    data class ExpensesTypeChanged(val expensesNumber:Int): ExpensesRegistrationEvent()
    data class AmountPayedChanged(val amountPayed:String):ExpensesRegistrationEvent()
}