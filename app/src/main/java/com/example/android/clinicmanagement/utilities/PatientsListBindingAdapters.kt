package com.example.android.clinicmanagement.utilities

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.PatientStatus
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.patientForm.PatientFormViewModel
import com.example.android.clinicmanagement.patientForm.PatientRegistrationEvent
import com.example.android.clinicmanagement.patientsList.FilterCriteriaChangeEvent
import com.example.android.clinicmanagement.patientsList.PatientsViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


//Binding Adapters for PatientsListItems
@BindingAdapter("avatarImage")
fun ImageView.setAvatarImage(patientStatus: PatientStatus?) {
    if (patientStatus != null) {
        val imageSource: Int
        with(patientStatus) {
            imageSource = when {
                age in 1..3 -> R.drawable.avatar_patient_baby
                age in 4..17 && gender == MALE_CHARACTER -> R.drawable.avatar_patient_boy
                age in 4..17 && gender == FEMALE_CHARACTER -> R.drawable.avatar_patient_girl
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

@BindingAdapter("patientFullName")
fun TextView.setPatientFullName(patientStatus: PatientStatus?) {
    if (patientStatus != null) {
        this.text = getFormattedFullName(patientStatus.firstName,patientStatus.lastName)
    }else{
        this.text ="--"
    }
}

@BindingAdapter("completionStatus")
fun TextView.setCompletionStatus(patientStatus: PatientStatus?) {
    if (patientStatus != null) {
        if (patientStatus.invNumber != null) {
            this.setText(R.string.sessions_completion_completed)
            this.setTextColor(ContextCompat.getColor(context, R.color.cerulean))
        } else {
            this.setText(R.string.sessions_completion_in_progress)
            this.setTextColor(ContextCompat.getColor(context, R.color.sunrise_orange))
        }
    }else{
        this.text ="--"
    }
}

@BindingAdapter("diagnosis")
fun TextView.setDiagnosis(patientStatus: PatientStatus?) {
    if (patientStatus != null) {
        this.setText(patientStatus.diagnosis)
    }else{
        this.text ="--"
    }
}

@BindingAdapter("lastSessionDate")
fun TextView.setLastSessionDate(patientStatus: PatientStatus?) {
    if (patientStatus != null) {
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        this.text = patientStatus.lastSession?.let {
            formatter.format(patientStatus.lastSession.times(1000))
        } ?: "--"
    } else {
        this.text = "--"
    }

}

@BindingAdapter("lastSessionTime")
fun TextView.setLastSessionTime(patientStatus: PatientStatus?) {
    if (patientStatus != null) {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        this.text = patientStatus.lastSession?.let {
            formatter.format(patientStatus.lastSession.times(1000))
        } ?: "--"
    } else {
        this.text = "--"
    }
}

////---
////Binding Adapters for patients fragment
@BindingAdapter("onFilterCriteriaChangedEvent")
fun TextInputEditText.setOnFilterCriteriaChangedAction(viewModel: PatientsViewModel) {
    fun getEvent(text: CharSequence): FilterCriteriaChangeEvent {
        //for each id we check that the changed text is blank or not,
        //if it is blank we pass a null value
        return when (this.id) {
            R.id.text_filter_first_name -> FilterCriteriaChangeEvent.FirstNameChanged(
                getStringOrNull(
                    text.toString()
                )
            )
            R.id.text_filter_last_name -> FilterCriteriaChangeEvent.LastNameChanged(
                getStringOrNull(
                    text.toString()
                )
            )
            R.id.text_filter_consultation_date_range -> {
                getStringOrNull(text.toString())?.let {
                    val dateRange = it.split(" → ")
                    FilterCriteriaChangeEvent.ConsultationDateChanged(dateRange[0], dateRange[1])
                } ?: FilterCriteriaChangeEvent.ConsultationDateChanged(null, null)
            }
            R.id.text_filter_diagnosis -> FilterCriteriaChangeEvent.DiagnosisChanged(
                getStringOrNull(
                    text.toString()
                )
            )
            else -> throw java.lang.Exception("No filter edittext Id matches")
        }
    }
    this.doOnTextChanged { text, start, before, count ->
        if (text != null) {
            viewModel.onEvent(getEvent(text))
        }
    }
}


//method to set text to the current range of age to always match the range slider
//when the fragment is initialized
@BindingAdapter("initialAgeTitleText")
fun TextView.setInitialAgeTitleText(rangeSlider: RangeSlider){
    val (ageStart, ageEnd) = rangeSlider.values.map { it.roundToInt() }
    this.text =
        resources.getString(
            R.string.title_age_range,
            ageStart,
            ageEnd
        )
}




@BindingAdapter("onFilterCriteriaChangedEvent", "titleAgeRange")
fun RangeSlider.setOnFilterCriteriaChangedAction(
    viewModel: PatientsViewModel,
    titleAgeRange: TextView
) {
    this.addOnChangeListener { _, _, _ ->
        val (ageStart, ageEnd) = this.values.map { it.roundToInt() }
        titleAgeRange.text =
            resources.getString(
                R.string.title_age_range,
                ageStart,
                ageEnd
            )
        if (ageStart == 0 && ageEnd == 100) {
            viewModel.onEvent(
                FilterCriteriaChangeEvent.AgeChanged(
                    null,
                    null
                )
            )
        } else {
            viewModel.onEvent(
                FilterCriteriaChangeEvent.AgeChanged(
                    ageStart,
                    ageEnd
                )
            )
        }
    }
}




@BindingAdapter("onFilterCriteriaChangedEvent")
fun ChipGroup.setOnFilterCriteriaChangedAction(viewModel: PatientsViewModel) {
    this.setOnCheckedStateChangeListener { chipGroup, checkedId ->
        val filterCriteriaChangeEvent: FilterCriteriaChangeEvent

        when (chipGroup.id) {
            R.id.chip_group_gender -> {

                filterCriteriaChangeEvent = if (checkedId.isNotEmpty()) {
                    when (checkedId[0]) {
                        R.id.chip_male -> FilterCriteriaChangeEvent.GenderChanged(MALE_CHARACTER)
                        R.id.chip_female -> FilterCriteriaChangeEvent.GenderChanged(FEMALE_CHARACTER)
                        else -> throw java.lang.Exception("No filter chip Id matches")
                    }
                } else {
                    FilterCriteriaChangeEvent.GenderChanged(null)
                }


            }
            R.id.chip_group_sessions_completion -> {

                filterCriteriaChangeEvent = if (checkedId.isNotEmpty()) {
                    when (checkedId[0]) {
                        R.id.chip_completed -> FilterCriteriaChangeEvent.SessionsCompletionChanged(
                            SESSIONS_COMPLETION_STATE_COMPLETED
                        )
                        R.id.chip_in_progress -> FilterCriteriaChangeEvent.SessionsCompletionChanged(
                            SESSIONS_COMPLETION_STATE_IN_PROGRESS
                        )
                        else -> throw java.lang.Exception("No filter chip Id matches")
                    }
                } else {
                    FilterCriteriaChangeEvent.SessionsCompletionChanged(null)
                }

            }
            else -> {
                throw java.lang.Exception("No chipGroup Id matches")
            }
        }

        viewModel.onEvent(filterCriteriaChangeEvent)
    }
}

//Binding adapter for setting the right text for text_result textView
@BindingAdapter("resultsCountText")
fun TextView.setResultsCountText(patientsCount:Int?){
   this.text = patientsCount?.let {
       when (patientsCount) {
           -1 -> {
               context.getString(R.string.patients_filter_no_criteria)
           }
           0 -> {
               context.getString(R.string.patients_filter_no_results)
           }
           else -> {
               resources.getQuantityString(R.plurals.patients_filter_results_count, patientsCount, patientsCount)
           }
       }
    }?:  context.getString(R.string.patients_filter_results_count_loading)
}







