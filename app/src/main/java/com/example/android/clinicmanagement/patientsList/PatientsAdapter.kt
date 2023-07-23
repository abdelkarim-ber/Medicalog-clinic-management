package com.example.android.clinicmanagement.patientsList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.PatientStatus
import com.example.android.clinicmanagement.databinding.ListItemPatientsBinding
import com.example.android.clinicmanagement.expenditures.ExpenditureCategoryAdapter.ViewHolder.Companion.recyclerView

class PatientsAdapter(private val patientClickListener: PatientClickListener) :
    PagingDataAdapter<PatientStatus, PatientsAdapter.ViewHolder>(PATIENT_STATUS_DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item , patientClickListener)
        }
    }


    class ViewHolder private constructor(val binding: ListItemPatientsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(patientStatus: PatientStatus, patientClickListener: PatientClickListener) {
            binding.patientClickListener = patientClickListener
            binding.patientStatus = patientStatus
            binding.patientId = patientStatus.id
            binding.executePendingBindings()
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

    companion object {
        private val PATIENT_STATUS_DIFF_CALLBACK = object : DiffUtil.ItemCallback<PatientStatus>() {
            override fun areItemsTheSame(oldItem: PatientStatus, newItem: PatientStatus): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PatientStatus,
                newItem: PatientStatus
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    fun interface PatientClickListener {
        fun onClick(view: View, patientId: Long)
    }

}