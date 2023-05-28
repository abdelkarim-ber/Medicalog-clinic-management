package com.example.android.clinicmanagement.domain

import com.example.android.clinicmanagement.data.models.Patient
import com.example.android.clinicmanagement.data.repositories.PatientRepository
import com.example.android.clinicmanagement.patientForm.PatientFormDataState
import com.example.android.clinicmanagement.utilities.FEMALE_CHARACTER
import com.example.android.clinicmanagement.utilities.MALE_CHARACTER
import com.example.android.clinicmanagement.utilities.convertDateStringToDateSeconds

class SubmitPatientInfoUseCase(private val patientRepository: PatientRepository) {
    suspend operator fun invoke(patientFormDataState: PatientFormDataState, patientId: Long) {
        var patient = Patient(
            firstName = patientFormDataState.firstName,
            lastName = patientFormDataState.lastName,
            age = patientFormDataState.age.toInt(),
            gender = when(patientFormDataState.gender){
                0 -> MALE_CHARACTER
                else -> FEMALE_CHARACTER
            },
            phoneNumber = patientFormDataState.phoneNumber,
            consultationDateInSeconds = convertDateStringToDateSeconds(patientFormDataState.consultationDate),
            doctorFullName = patientFormDataState.doctorFullName,
            diagnosis = patientFormDataState.diagnosis,
            frequency = patientFormDataState.frequency,
            sessionCount = patientFormDataState.sessionCount.toInt(),
            sessionPrice = patientFormDataState.sessionPrice.toInt(),
            socialCoverage = patientFormDataState.socialCoverage
        )
        if (patientId != -1L){
            patient = patient.copy(id = patientId)
            patientRepository.updatePatient(patient)
        }else{
            patientRepository.addPatient(patient)
        }
    }
}