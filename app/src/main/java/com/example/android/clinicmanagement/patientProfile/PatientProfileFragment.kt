package com.example.android.clinicmanagement.patientProfile

import android.graphics.Color
import android.icu.util.UniversalTimeScale.toLong
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            // Scope the transition to a view in the hierarchy so we know it will be added under
            // the bottom app bar but over the elevation scale of the exiting HomeFragment.
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.clinicmanagement_motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(ContextCompat.getColor(requireContext(),R.color.white))
        }
    }

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
          iconNavigation.setOnClickListener{findNavController().navigateUp()}
            viewModel = patientProfileViewModel
            patientId = args.patientKey
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }
}