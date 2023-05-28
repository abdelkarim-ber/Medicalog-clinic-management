package com.example.android.clinicmanagement.domain

import androidx.annotation.StringRes

data class ValidationResult (
    val isSuccessful:Boolean,
    @StringRes val errorMessage:Int?=null
)