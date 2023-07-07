package com.example.android.clinicmanagement.patientsList


data class FilterDataState(
    val firstName: String? = null,
    val lastName: String? = null,
    val ageRangeStart: Int? = null,
    val ageRangeEnd: Int? = null,
    val gender: Char? = null,
    val diagnosis: String? = null,
    val consultDateRangeStart: String? = null,
    val consultDateRangeEnd: String? = null,
    val sessionsCompletionState: Int? = null,
)