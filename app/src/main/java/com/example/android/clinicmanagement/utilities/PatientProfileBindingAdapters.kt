package com.example.android.clinicmanagement.utilities

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.PatientDetails
import java.util.*

//Binding Adapters for PatientsListItems
@BindingAdapter("profileAvatarImage")
fun ImageView.setProfileAvatarImage(patientDetails: PatientDetails?) {

    if(patientDetails!= null){
        val imageSource: Int
        with(patientDetails) {
            imageSource = when {
                age in 0..17 && gender == MALE_CHARACTER -> R.drawable.avatar_patient_boy
                age in 0..17 && gender == FEMALE_CHARACTER -> R.drawable.avatar_patient_girl
                age in 18..45 && gender == MALE_CHARACTER -> R.drawable.avatar_patient_young_man
                age in 18..45 && gender == FEMALE_CHARACTER -> R.drawable.avatar_patient_young_woman
                age >= 46 && gender == MALE_CHARACTER -> R.drawable.avatar_patient_old_man
                age >= 46 && gender == FEMALE_CHARACTER -> R.drawable.avatar_patient_old_woman
                else -> throw Exception("No avatar image matched")
            }
        }
        this.setImageResource(imageSource)
    }

}

@BindingAdapter("profileInfo")
fun TextView.setProfileInfo(patientDetails: PatientDetails?) {
    val id = this.id
    if (patientDetails != null) {
        this.text = with(patientDetails) {

            when (id) {

                R.id.text_patient_full_name -> getFormattedFullName(firstName,lastName)


                R.id.text_done_sessions -> resources.getQuantityString(R.plurals.done_sessions_field, doneSessions, doneSessions)

                R.id.text_amount_payed -> context.getString(R.string.moroccan_currency_with_number,totalAmountPayed ?: 0 )


                R.id.text_phone_number -> phoneNumber

                R.id.text_consultation_date -> convertDateSecondsToDateString(
                    consultationDateInSeconds
                )

                R.id.text_gender -> {
                    if (gender == MALE_CHARACTER) context.getString(R.string.male) else context.getString(
                        R.string.female
                    )
                }

                R.id.text_age -> context.getString(R.string.years_old_with_number,age)

                R.id.text_treating_doctor -> context.getString(R.string.doctor_prefix_with_text,doctorFullName)

                R.id.text_diagnosis -> diagnosis

                R.id.text_session_count -> resources.getQuantityString(R.plurals.done_sessions_field, sessionCount, sessionCount)

                R.id.text_frequency -> frequency

                R.id.text_social_coverage -> socialCoverage.ifBlank { "--" }

                R.id.text_session_price -> context.getString(R.string.moroccan_currency_with_number,sessionPrice)

                R.id.text_total -> context.getString(R.string.moroccan_currency_with_number,sessionPrice * sessionCount)

                else -> throw java.lang.Exception("No Patient profile TextView Id matches")
            }
        }
    }

}