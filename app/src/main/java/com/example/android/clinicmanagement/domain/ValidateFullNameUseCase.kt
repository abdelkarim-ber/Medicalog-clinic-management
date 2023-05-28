package com.example.android.clinicmanagement.domain

import android.content.Context
import androidx.core.widget.doOnTextChanged
import com.example.android.clinicmanagement.R


class ValidateFullNameUseCase {

    operator fun invoke(fullName: String): ValidationResult {
        return if (fullName.isBlank()) {
            ValidationResult(
                isSuccessful = false,
                errorMessage = R.string.form_error_field_required
            )
        } else if (fullName.any { !it.isLetter() && !it.isWhitespace() }) {
            ValidationResult(
                isSuccessful = false,
                errorMessage = R.string.form_error_no_special_char
            )
        } else
            ValidationResult(isSuccessful = true)

    }
}