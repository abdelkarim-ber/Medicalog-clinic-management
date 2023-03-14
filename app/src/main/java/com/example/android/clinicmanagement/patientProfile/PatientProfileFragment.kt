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
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding
import com.example.android.clinicmanagement.patientsList.PatientsFragmentDirections
import com.example.android.clinicmanagement.patientsList.PatientsViewModel
import com.example.android.clinicmanagement.patientsList.PatientsViewModelFactory
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale


class PatientProfileFragment : Fragment() {

    val args: PatientProfileFragmentArgs by navArgs()
    lateinit var binding: FragmentPatientProfileBinding
    lateinit var patientProfileViewModel: PatientProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            interpolator = FastOutLinearInInterpolator()
            duration =
                resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(ContextCompat.getColor(requireContext(), R.color.white))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentPatientProfileBinding?>(
            inflater, R.layout.fragment_patient_profile, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val viewModelFactory = PatientProfileViewModelFactory()

        patientProfileViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PatientProfileViewModel::class.java)



        patientProfileViewModel.navigateToPatientInfoUpdate.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
                resetTransition()
                findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToPatientForm(
                        patientId
                    )
                )
                patientProfileViewModel.onPatientInfoUpdateNavigated()
            }
        }
        patientProfileViewModel.navigateToPatientHistory.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
                resetTransition()
                findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToPatientHistory(
                        patientId
                    )
                )
                patientProfileViewModel.onPatientHistoryNavigated()
            }
        }
        patientProfileViewModel.navigateToReceipt.observe(viewLifecycleOwner) { receiptInfo ->
            receiptInfo?.let {
                resetTransition()
                findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToReceipt(
                        receiptInfo.first,
                        receiptInfo.second
                    )
                )
                patientProfileViewModel.onReceiptNavigated()
            }
        }
        patientProfileViewModel.navigateToAddNewSession.observe(viewLifecycleOwner) { fabInfo ->
            fabInfo?.let {
                val fabView = fabInfo.first
                val patientId = fabInfo.second

                exitTransition = MaterialElevationScale(false).apply {
                    duration =
                        resources.getInteger(R.integer.clinicmanagement_motion_duration_medium)
                            .toLong()
                }
                reenterTransition = MaterialElevationScale(true).apply {
                    duration =
                        resources.getInteger(R.integer.clinicmanagement_motion_duration_medium)
                            .toLong()
                }
                val newSessionTransitionName = getString(R.string.new_session_transition_name)
                val extras = FragmentNavigatorExtras(fabView to newSessionTransitionName)

                this.findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToNewSession(patientId),
                    extras
                )
                patientProfileViewModel.onAddNewSessionNavigated()
            }
        }


        binding.apply {
            listPatientInfo.adapter = PatientInfoAdapter()
            iconNavigation.setOnClickListener { findNavController().navigateUp() }
            viewModel = patientProfileViewModel
            patientId = args.patientKey
        }


    }

    private fun resetTransition() {
        //reset to transitions set in navigation graph
        if (exitTransition != null || reenterTransition != null)
            exitTransition = null
            reenterTransition = null
    }
}