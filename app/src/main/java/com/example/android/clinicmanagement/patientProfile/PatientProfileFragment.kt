package com.example.android.clinicmanagement.patientProfile

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding
import com.example.android.clinicmanagement.patientsList.PatientsViewModel
import com.example.android.clinicmanagement.patientsList.PatientsViewModelFactory
import com.google.android.material.transition.MaterialContainerTransform


class PatientProfileFragment : Fragment() {

    val args: PatientProfileFragmentArgs by navArgs()
    lateinit var binding: FragmentPatientProfileBinding
    lateinit var patientProfileViewModel: PatientProfileViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModelFactory = PatientProfileViewModelFactory()

        patientProfileViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PatientProfileViewModel::class.java)


        binding = DataBindingUtil.inflate<FragmentPatientProfileBinding?>(
            inflater, R.layout.fragment_patient_profile, container, false
        )

        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.modify -> patientProfileViewModel.onPatientInfoUpdateClicked(args.patientKey)
                R.id.history -> patientProfileViewModel.onPatientHistoryClicked(args.patientKey)
            }
            true
        }

        patientProfileViewModel.navigateToPatientInfoUpdate.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
                findNavController().navigate(PatientProfileFragmentDirections.actionPatientProfileToPatientForm(patientId) )
                patientProfileViewModel.onPatientInfoUpdateNavigated()
            }
        }
        patientProfileViewModel.navigateToPatientHistory.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
                findNavController().navigate(PatientProfileFragmentDirections.actionPatientProfileToPatientHistory(patientId) )
                patientProfileViewModel.onPatientHistoryNavigated()
            }
        }
        patientProfileViewModel.navigateToReceipt.observe(viewLifecycleOwner) { receiptInfo ->
            receiptInfo?.let {
                findNavController().navigate(PatientProfileFragmentDirections.actionPatientProfileToReceipt(receiptInfo.first,receiptInfo.second) )
                patientProfileViewModel.onReceiptNavigated()
            }
        }

        binding.apply{
            listPatientInfo.adapter = PatientInfoAdapter()
            toolBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            viewModel = patientProfileViewModel
            patientId = args.patientKey
        }



        return binding.root
    }

}