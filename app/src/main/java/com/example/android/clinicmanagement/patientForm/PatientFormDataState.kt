package com.example.android.clinicmanagement.patientForm

import androidx.lifecycle.MutableLiveData

data class PatientFormDataState(
    val firstName: String = "",
    val errorFirstName: MutableLiveData<Int?>? = MutableLiveData(null),
    val lastName: String = "",
    val errorLastName: MutableLiveData<Int?>? = MutableLiveData(null),
    val age: String = "",
    val errorAge: MutableLiveData<Int?>? = MutableLiveData(null),
    val gender: Int = -1,
    val errorGender: MutableLiveData<Int?>? = MutableLiveData(null),
    val phoneNumber: String = "",
    val errorPhoneNumber: MutableLiveData<Int?>? = MutableLiveData(null),
    val consultationDate: String = "",
    val errorConsultationDate: MutableLiveData<Int?>? = MutableLiveData(null),
    val doctorFullName: String = "",
    val errorDoctorFullName: MutableLiveData<Int?>? = MutableLiveData(null),
    val diagnosis: String = "",
    val errorDiagnosis: MutableLiveData<Int?>? = MutableLiveData(null),
    val frequency: String = "",
    val errorFrequency: MutableLiveData<Int?>? = MutableLiveData(null),
    val sessionCount: String = "",
    val errorSessionCount: MutableLiveData<Int?>? = MutableLiveData(null),
    val sessionPrice: String = "",
    val errorSessionPrice: MutableLiveData<Int?>? = MutableLiveData(null),
    val socialCoverage: String = "",
    val errorSocialCoverage: MutableLiveData<Int?>? = MutableLiveData(null)
)