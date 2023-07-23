package com.example.android.clinicmanagement.patientHistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.PatientStatus
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.databinding.ListItemPatientHistoryBinding
import com.example.android.clinicmanagement.patientsList.PatientsAdapter

class PatientHistoryAdapter(private val sessionDeleteClickListener: SessionDeleteClickListener) :
    PagingDataAdapter<Session, PatientHistoryAdapter.ViewHolder>(SESSION_DIFF_CALLBACK) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item , sessionDeleteClickListener)
        }
    }


    class ViewHolder private constructor(val binding: ListItemPatientHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Session, sessionDeleteClickListener: SessionDeleteClickListener) {
            binding.session = data
            binding.sessionDeleteClickListener = sessionDeleteClickListener
            binding.executePendingBindings()
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

    companion object {
        private val SESSION_DIFF_CALLBACK = object : DiffUtil.ItemCallback<Session>() {
            override fun areItemsTheSame(oldItem: Session, newItem: Session): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Session,
                newItem: Session
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    fun interface SessionDeleteClickListener {
        fun onClick(session: Session)
    }
}