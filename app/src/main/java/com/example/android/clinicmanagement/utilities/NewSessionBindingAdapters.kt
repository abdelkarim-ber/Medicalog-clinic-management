package com.example.android.clinicmanagement.utilities

import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.patientForm.PatientFormViewModel
import com.example.android.clinicmanagement.patientForm.PatientRegistrationEvent
import com.example.android.clinicmanagement.session.NewSessionRegistrationEvent
import com.example.android.clinicmanagement.session.NewSessionViewModel
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("onNewSessionDataChanged")
fun TextInputEditText.setOnNewSessionDataChangedAction(viewModel: NewSessionViewModel) {
    fun getEvent(text: CharSequence): NewSessionRegistrationEvent {
        return when (this.id) {
            R.id.text_date -> NewSessionRegistrationEvent.DateChanged(text.toString())
            R.id.text_time -> NewSessionRegistrationEvent.TimeChanged(text.toString())
            R.id.text_amount_payed -> NewSessionRegistrationEvent.AmountPayedChanged(text.toString())
            else -> throw java.lang.Exception("No Edittext Id matches")
        }
    }
    this.doOnTextChanged { text, start, before, count ->
        if (text != null) {
            viewModel.onEvent(getEvent(text))
        }
    }
}