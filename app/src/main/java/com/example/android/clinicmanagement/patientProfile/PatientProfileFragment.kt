package com.example.android.clinicmanagement.patientProfile

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialContainerTransform


class PatientProfileFragment : Fragment() {

    private val args: PatientProfileFragmentArgs by navArgs()
    private lateinit var binding: FragmentPatientProfileBinding
    private lateinit var patientProfileViewModel: PatientProfileViewModel

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

        binding = DataBindingUtil.inflate(
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


        //Set a clickListener on the invoice generating button to show an alert
        //to the user if he attempts to generate an invoice for the current patient
        //for the first time as this action is irreversible.
        binding.invoiceCard.setOnClickListener {
            if (binding.fabAddSession.isVisible){
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.dialog_invoice_confirmation_title))
                    .setMessage(resources.getString(R.string.dialog_invoice_confirmation_message))
                    .setPositiveButton(resources.getString(R.string.dialog_confirm)) { dialog, which ->
                        patientProfileViewModel.onReceiptInvoiceClicked(args.patientKey)
                    }
                    .setNegativeButton(resources.getString(R.string.dialog_cancel)) { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }else{
                patientProfileViewModel.onReceiptInvoiceClicked(args.patientKey)
            }

        }
        // Add an Observer on the state variable for Navigating to patient form screen to
        // update patient informations when the update button is clicked.
        patientProfileViewModel.navigateToPatientInfoUpdate.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
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
        patientProfileViewModel.navigateToAddNewSession.observe(viewLifecycleOwner) { patientId ->
            patientId?.let {
                this.findNavController().navigate(
                    PatientProfileFragmentDirections.actionPatientProfileToNewSession(patientId)
                )
                patientProfileViewModel.onAddNewSessionNavigated()
            }
        }

    }

}