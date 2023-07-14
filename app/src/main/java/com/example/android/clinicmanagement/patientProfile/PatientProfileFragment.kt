package com.example.android.clinicmanagement.patientProfile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding
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


        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val viewModelFactory = PatientProfileViewModelFactory(args.patientKey,appContainer.patientRepository)

        patientProfileViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PatientProfileViewModel::class.java)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentPatientProfileBinding?>(
            inflater, R.layout.fragment_patient_profile, container, false
        )
        binding.apply {
            viewModel = patientProfileViewModel
            patientId = args.patientKey
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.iconReturn.setOnClickListener { findNavController().navigateUp() }



        // Add an Observer on the state variable for Navigating to patient form screen to
        // update patient informations when the update button is clicked.
        patientProfileViewModel.navigateToPatientInfoUpdate.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
                resetTransitions()
                findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToPatientForm(
                        patientId
                    )
                )
                patientProfileViewModel.onPatientInfoUpdateNavigated()
            }
        }
        // Add an Observer on the state variable for Navigating to patient history screen to
        // show patient's list of done sessions info when the history button is clicked.
        patientProfileViewModel.navigateToPatientHistory.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
                resetTransitions()
                findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToPatientHistory(
                        patientId
                    )
                )
                patientProfileViewModel.onPatientHistoryNavigated()
            }
        }
        // Add an Observer on the state variable for Navigating to Receipt screen whether for
        // generating a Quotation or an Invoice , when the quotation or invoice button is clicked.
        patientProfileViewModel.navigateToReceipt.observe(viewLifecycleOwner) { receiptInfo ->
            receiptInfo?.let {
                resetTransitions()
                findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToReceipt(
                        receiptInfo.first,
                        receiptInfo.second
                    )
                )
                patientProfileViewModel.onReceiptNavigated()
            }
        }
        // Add an Observer on the state variable for Navigating to New Session screen to
        // add patient's done session info when the add session fab button is clicked.
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

    }

    /**
     * Reset transitions to the ones set in navigation graph
     */
    private fun resetTransitions() {
        if (exitTransition != null || reenterTransition != null)
            exitTransition = null
            reenterTransition = null
    }
}