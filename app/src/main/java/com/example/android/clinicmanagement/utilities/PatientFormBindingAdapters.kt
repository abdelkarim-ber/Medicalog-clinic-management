package com.example.android.clinicmanagement.utilities

import android.content.Context
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.patientForm.PatientFormDataState
import com.example.android.clinicmanagement.patientForm.PatientFormViewModel
import com.example.android.clinicmanagement.patientForm.PatientRegistrationEvent
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("errorTextResource")
fun TextInputLayout.setErrorText(errorTextResource: Int?) {
    this.error = errorTextResource?.let { context.getString(it) }
}

@BindingAdapter("onTextChangedEvent")
fun TextInputEditText.setOnTextChangedAction(viewModel: PatientFormViewModel) {
    fun getEvent(text: CharSequence): PatientRegistrationEvent {
        return when (this.id) {
            R.id.text_first_name -> PatientRegistrationEvent.FirstNameChanged(text.toString())
            R.id.text_last_name -> PatientRegistrationEvent.LastNameChanged(text.toString())
            R.id.text_age -> PatientRegistrationEvent.AgeChanged(text.toString())
            R.id.text_phone_number -> PatientRegistrationEvent.PhoneNumberChanged(text.toString())
            R.id.text_consultation_date -> PatientRegistrationEvent.ConsultationDateChanged(text.toString())
            R.id.text_doctor_fullName -> PatientRegistrationEvent.DoctorFullNameChanged(text.toString())
            R.id.text_diagnostic -> PatientRegistrationEvent.DiagnosisChanged(text.toString())
            R.id.text_frequency -> PatientRegistrationEvent.FrequencyChanged(text.toString())
            R.id.text_session_count -> PatientRegistrationEvent.SessionCountChanged(text.toString())
            R.id.text_session_price -> PatientRegistrationEvent.SessionPriceChanged(text.toString())
            R.id.text_social_coverage -> PatientRegistrationEvent.SocialCoverageChanged(text.toString())
            else -> throw java.lang.Exception("No Edittext Id matches")
        }
    }
    this.doOnTextChanged { text, start, before, count ->
        if (text != null) {
            viewModel.onEvent(getEvent(text))
        }
    }
}

@BindingAdapter("onTextChangedEvent")
fun AutoCompleteTextView.setOnTextChangedAction(viewModel: PatientFormViewModel) {
    this.setOnItemClickListener { parent, view, position, id ->
        viewModel.onEvent(PatientRegistrationEvent.GenderChanged(position))
    }
}

@BindingAdapter("previousValue")
fun AutoCompleteTextView.setPreviousValue(patientData: PatientFormDataState?) {
        if (patientData != null) {
            this.setText(adapter.getItem(patientData.gender).toString(),false)
        }
}

@BindingAdapter("previousValue")
fun TextInputEditText.setPreviousValue(patientData: PatientFormDataState?) {
        if (patientData != null) {
            this.setText(
                when (this.id) {
                    R.id.text_first_name -> patientData.firstName
                    R.id.text_last_name -> patientData.lastName
                    R.id.text_age -> patientData.age
                    R.id.text_phone_number -> patientData.phoneNumber
                    R.id.text_consultation_date -> patientData.consultationDate
                    R.id.text_doctor_fullName -> patientData.doctorFullName
                    R.id.text_diagnostic -> patientData.diagnosis
                    R.id.text_frequency -> patientData.frequency
                    R.id.text_session_count -> patientData.sessionCount
                    R.id.text_session_price -> patientData.sessionPrice
                    R.id.text_social_coverage -> patientData.socialCoverage
                    else -> throw java.lang.Exception("No Edittext Id matches")
                }
            )
    }
}
