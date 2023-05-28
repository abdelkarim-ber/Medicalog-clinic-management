package com.example.android.clinicmanagement.domain

import android.content.Context
import com.example.android.clinicmanagement.R

class ValidateAgeUseCase {
    operator fun invoke(text: String): ValidationResult {
        return if (text.isBlank() || text.toInt() == 0) {
            ValidationResult(
                isSuccessful = false,
                errorMessage = R.string.form_error_field_required
            )
        } else if (text.toInt() > 100) {
            ValidationResult(
                isSuccessful = false,
                errorMessage = R.string.form_error_age
            )
        } else
            ValidationResult(isSuccessful = true)
    }
}