package com.example.android.clinicmanagement.patientForm

sealed class PatientRegistrationEvent {
    data class FirstNameChanged(val firstName:String):PatientRegistrationEvent()
    data class LastNameChanged(val lastName:String):PatientRegistrationEvent()
    data class AgeChanged(val age:String):PatientRegistrationEvent()
    data class GenderChanged(val gender:Int):PatientRegistrationEvent()
    data class PhoneNumberChanged(val phoneNumber:String):PatientRegistrationEvent()
    data class ConsultationDateChanged(val consultationDate:String):PatientRegistrationEvent()
    data class DoctorFullNameChanged(val doctorFullName:String):PatientRegistrationEvent()
    data class DiagnosisChanged(val diagnosis:String):PatientRegistrationEvent()
    data class FrequencyChanged(val frequency:String):PatientRegistrationEvent()
    data class SessionCountChanged(val sessionCount:String):PatientRegistrationEvent()
    data class SessionPriceChanged(val sessionPrice:String):PatientRegistrationEvent()
    data class SocialCoverageChanged(val socialCoverage:String):PatientRegistrationEvent()
}