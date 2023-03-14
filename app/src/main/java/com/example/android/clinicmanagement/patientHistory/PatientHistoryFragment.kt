package com.example.android.clinicmanagement.patientHistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientHistoryBinding
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding
import com.example.android.clinicmanagement.patientProfile.PatientInfoAdapter


class PatientHistoryFragment : Fragment() {

lateinit var binding: FragmentPatientHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patient_history, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listPatientHistory.adapter = PatientHistoryAdapter()
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}