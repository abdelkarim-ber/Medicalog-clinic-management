package com.example.android.clinicmanagement.patient

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.ListItemExpenditureCategoryBinding
import com.example.android.clinicmanagement.databinding.ListItemPatientsBinding
import com.example.android.clinicmanagement.expenditures.Expenditure
import com.example.android.clinicmanagement.expenditures.ExpenditureAdapter

class PatientAdapter : RecyclerView.Adapter<PatientAdapter.ViewHolder>() {
    var data = listOf<Patient>(
        Patient("Karim Sadiki", 40, "m", "In progress", 4500),
        Patient("Khadija Mrzouki", 20, "f", "Done", 8000),
        Patient("Aatif khelfi", 60, "m", "In progress", 100),
        Patient("Saad Mnsouri", 20, "m", "Done", 3000),
        Patient("Samira Jamili", 30, "f", "In progress", 600),
        Patient("Aamir Chakiri ", 10, "m", "Done", 7800),
        Patient("Leyla Aabidi ", 30, "f", "In progress", 6000),
    )


    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position))
    }


    class ViewHolder private constructor(val binding: ListItemPatientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Patient) {
            data.apply {
                binding.imgAvatarPatient.setImageResource(
                    when {
                        age <= 10 && gender == "m" -> R.drawable.avatar_patient_boy
                        age <= 10 && gender == "f" -> R.drawable.avatar_patient_girl
                        age in 11..40 && gender == "m" -> R.drawable.avatar_patient_young_man
                        age in 11..40 && gender == "f" -> R.drawable.avatar_patient_young_woman
                        age > 40 && gender == "m" -> R.drawable.avatar_patient_old_man
                        age > 40 && gender == "f" -> R.drawable.avatar_patient_old_woman
                        else -> throw Exception("")
                    }
                )
                binding.textPatientFullName.text = fullName
                binding.textPatientStatus.text = status
                binding.textPatientStatus.setTextColor( when (status) {
                    "In progress" -> ContextCompat.getColor(binding.root.context,R.color.sunrise_orange)
                    "Done" -> ContextCompat.getColor(binding.root.context,R.color.cerulean)
                    else -> ContextCompat.getColor(binding.root.context,R.color.midnight)
                }

                )
                binding.textPatientPayedAmount.text = "$amount DH"
                Log.d("patients adapter", " view holder bind method")
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListItemPatientsBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item_patients, parent, false
                )
                return ViewHolder(binding)
            }
        }

    }

    data class Patient(
        val fullName: String,
        val age: Int,
        val gender: String,
        val status: String,
        val amount: Int
    )

}