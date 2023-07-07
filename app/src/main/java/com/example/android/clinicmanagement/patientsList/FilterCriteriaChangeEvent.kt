package com.example.android.clinicmanagement.patientsList



sealed class FilterCriteriaChangeEvent {
    data class FirstNameChanged(val firstName:String?): FilterCriteriaChangeEvent()
    data class LastNameChanged(val lastName:String?):FilterCriteriaChangeEvent()
    data class AgeChanged(val ageStart:Int?,val ageEnd:Int? ):FilterCriteriaChangeEvent()
    data class GenderChanged(val gender:Char?):FilterCriteriaChangeEvent()
    data class ConsultationDateChanged(val consultationDateStart:String?,val consultationDateEnd:String?):FilterCriteriaChangeEvent()
    data class DiagnosisChanged(val diagnosis:String?):FilterCriteriaChangeEvent()
    /**
    *@param completion must be either 0 for In progress state , or other (like 1) for completed state
     */
    data class SessionsCompletionChanged(val sessionsCompletionState:Int?):FilterCriteriaChangeEvent()
}