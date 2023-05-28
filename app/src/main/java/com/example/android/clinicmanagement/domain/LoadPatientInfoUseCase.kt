package com.example.android.clinicmanagement.domain

import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.patientForm.PatientFormDataState
import com.example.android.clinicmanagement.utilities.MALE_CHARACTER
import com.example.android.clinicmanagement.utilities.convertDateSecondsToDateString

class LoadPatientInfoUseCase(private val patientRepository: PatientRepository) {
    suspend operator fun invoke(patientId: Long,initialPatientFormDataState:PatientFormDataState):PatientFormDataState {
        val patient = patientRepository.loadPatientWithId(patientId)
        return initialPatientFormDataState.copy(
            firstName = patient.firstName,
            lastName = patient.lastName,
            age = patient.age.toString(),
            gender = when(patient.gender){
                MALE_CHARACTER ->  0
                else -> 1
            },
            phoneNumber = patient.phoneNumber,
            consultationDate = convertDateSecondsToDateString(patient.consultationDateInSeconds),
            doctorFullName = patient.doctorFullName,
            diagnosis = patient.diagnosis,
            frequency = patient.frequency,
            sessionCount = patient.sessionCount.toString(),
            sessionPrice = patient.sessionPrice.toString(),
            socialCoverage = patient.socialCoverage
        )

    }
}