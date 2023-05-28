package com.example.android.clinicmanagement.domain

import android.content.Context
import com.example.android.clinicmanagement.R

class ValidateTextUseCase {
    operator fun invoke(text: String): ValidationResult {
        return if (text.isBlank()) {
            ValidationResult(
                isSuccessful = false,
                errorMessage = R.string.form_error_field_required
            )
        } else
            ValidationResult(isSuccessful = true)
}
}