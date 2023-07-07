package com.example.android.clinicmanagement.patientProfile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.ListItemPatientInfosBinding

class PatientInfoAdapter : RecyclerView.Adapter<PatientInfoAdapter.ViewHolder>() {

    val resources = listOf(
        R.string.age,
        R.string.phone_number,
        R.string.consultation_date,
        R.string.doctor,
        R.string.diagnosis,
        R.string.session_count,
        R.string.frequency,
        R.string.session_price,
        R.string.total,
        R.string.total_in_letters,
        R.string.social_coverage
    )



    val data = PatientInfo(
        20,
        "02326255454",
        "02 September 2020",
        "Sam Berg",
        "fracture",
        20,
        "once a week",
        50,
        1000,
        "one thousand",
        "cnss"
    )


    override fun getItemCount() = data.propertiesList.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(resources[position],data.propertiesList[position].toString())
    }


    class ViewHolder private constructor(val binding: ListItemPatientInfosBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(infoTitleResource:Int,infoText:String) {
           binding.titleInfo.text = binding.root.context.getString(infoTitleResource)
           binding.textInfo.text = infoText
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListItemPatientInfosBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item_patient_infos, parent, false
                )
                return ViewHolder(binding)
            }
        }

    }

    data class PatientInfo(
        val age: Int,
        val phoneNumber: String,
        val consultationDate: String,
        val doctor: String,
        val diagnosis: String,
        val sessionsNumber: Int,
        val frequency: String,
        val sessionPriceInDH: Int,
        val total: Int,
        val totalInLetters: String,
        val socialCoverage: String
    ){

        val propertiesList = listOf(
        age,
        phoneNumber,
        consultationDate,
        doctor,
        diagnosis,
        sessionsNumber,
        frequency,
        sessionPriceInDH,
        total,
        totalInLetters,
        socialCoverage)
    }

}