package com.example.android.clinicmanagement.session

sealed class NewSessionRegistrationEvent{
    data class DateChanged(val date:String):NewSessionRegistrationEvent()
    data class TimeChanged(val time:String):NewSessionRegistrationEvent()
    data class AmountPayedChanged(val amountPayed:String):NewSessionRegistrationEvent()
}
