package com.example.android.clinicmanagement.patientsList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.ListItemPatientsBinding

class PatientsAdapter(val patientListener:PatientListener) : RecyclerView.Adapter<PatientsAdapter.ViewHolder>() {
    var data = listOf<Patient>(
        Patient(1,"Karim Sadiki", 40, "m", "In progress", 4500),
        Patient(2,"Khadija Mrzouki", 20, "f", "Done", 8000),
        Patient(3,"Aatif khelfi", 60, "m", "In progress", 100),
        Patient(4,"Saad Mnsouri", 20, "m", "Done", 3000),
        Patient(5,"Samira Jamili", 30, "f", "In progress", 600),
        Patient(6,"Aamir Chakiri ", 10, "m", "Done", 7800),
        Patient(7,"Leyla Aabidi ", 30, "f", "In progress", 6000),
    )


    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data.get(position), patientListener)
    }


    class ViewHolder private constructor(val binding: ListItemPatientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Patient, patientListener:PatientListener) {
            data.apply {
                binding.layout.setOnClickListener{
                    patientListener.onClick(it,545454)
                }
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
                binding.number = id
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
        val id:Int,
        val fullName: String,
        val age: Int,
        val gender: String,
        val status: String,
        val amount: Int
    )

    fun interface PatientListener{
        fun onClick(view: View, patientId:Long)
    }

}