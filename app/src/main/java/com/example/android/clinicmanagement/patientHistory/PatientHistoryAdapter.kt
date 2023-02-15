package com.example.android.clinicmanagement.patientHistory

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.ListItemPatientHistoryBinding
import com.example.android.clinicmanagement.databinding.ListItemPatientsBinding
import com.example.android.clinicmanagement.patientsList.PatientAdapter

class PatientHistoryAdapter  : RecyclerView.Adapter<PatientHistoryAdapter.ViewHolder>() {
    var data = listOf(
        PatientHistory("02 Sep 2023", "01:22 PM", 2000,"Cash"),
        PatientHistory("02 Sep 2023", "04:22 PM", 3500, "Check"),
        PatientHistory("02 Sep 2023", "09:22 AM", 800, "Cash"),
        PatientHistory("02 Sep 2023", "01:22 PM", 0, "Payment delayed"),
        PatientHistory("02 Sep 2023", "11:22 AM", 2000, "Check"),
        PatientHistory("02 Sep 2023", "05:22 PM", 0, "Payment delayed"),
        PatientHistory("02 Sep 2023", "12:22 PM", 8500, "Cash"),
        PatientHistory("02 Sep 2023", "12:22 PM", 8500, "Cash"),
        PatientHistory("02 Sep 2023", "12:22 PM", 8500, "Cash"),
        PatientHistory("02 Sep 2023", "12:22 PM", 8500, "Cash"),
        PatientHistory("02 Sep 2023", "12:22 PM", 8500, "Cash")
    )


    override fun getItemCount() = data.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }


    class ViewHolder private constructor(val binding: ListItemPatientHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PatientHistory) {
            data.apply {
               binding.textDate.text  = date
               binding.textTime.text  = time
               binding.textAmountPayed.text  = "$amount DH"
               binding.textPaymentType.text  = paymentType
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: ListItemPatientHistoryBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item_patient_history, parent, false
                )
                return ViewHolder(binding)
            }
        }

    }

    data class PatientHistory(
        val date: String,
        val time: String,
        val amount: Int,
        val paymentType: String
    )

}