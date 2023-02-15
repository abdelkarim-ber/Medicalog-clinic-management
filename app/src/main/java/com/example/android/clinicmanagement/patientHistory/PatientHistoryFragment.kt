package com.example.android.clinicmanagement.patientHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientHistoryBinding
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding
import com.example.android.clinicmanagement.patientProfile.PatientInfoAdapter


class PatientHistoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPatientHistoryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patient_history, container, false
        )
        binding.listPatientHistory.adapter = PatientHistoryAdapter()
        return binding.root
    }

}