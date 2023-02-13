package com.example.android.clinicmanagement.patientProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding


class PatientProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPatientProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patient_profile, container, false
        )
        binding.listPatientInfo.adapter = PatientInfoAdapter()
        return binding.root
    }

}