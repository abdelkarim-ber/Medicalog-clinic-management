package com.example.android.clinicmanagement.patientHistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientHistoryBinding
import com.example.android.clinicmanagement.patientProfile.PatientProfileFragmentArgs
import com.example.android.clinicmanagement.patientsList.LoadStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class PatientHistoryFragment : Fragment() {

    private lateinit var binding: FragmentPatientHistoryBinding
    private lateinit var patientHistoryViewModel: PatientHistoryViewModel
    private val args: PatientProfileFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val patientHistoryViewModelFactory =
            PatientHistoryViewModelFactory(args.patientKey, appContainer.sessionsRepository)

        patientHistoryViewModel = ViewModelProvider(
            this, patientHistoryViewModelFactory
        ).get(PatientHistoryViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patient_history, container, false
        )
        binding.viewModel = patientHistoryViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val patientHistoryAdapter = PatientHistoryAdapter { session ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(resources.getString(R.string.dialog_deletion_confirmation_title))
                .setMessage(resources.getString(R.string.dialog_deletion_confirmation_message))
                .setPositiveButton(resources.getString(R.string.dialog_confirm)) { dialog, which ->
                    patientHistoryViewModel.deleteSession(session)
                }
                .setNegativeButton(resources.getString(R.string.dialog_cancel)) { dialog, which ->
                    dialog.dismiss()
                }
                .show()

        }

        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        //Collecting the load state flow.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                patientHistoryAdapter.loadStateFlow.collect { loadStates ->
                    //We check if the list has finished loading
                    //Then corresponding to the list item count we either show the place holder screen
                    // or the actual content.
                    if (loadStates.refresh is LoadState.NotLoading ) {
                        if (patientHistoryAdapter.itemCount == 0) {
                            patientHistoryViewModel.showEmptyScreen()
                        } else {
                            patientHistoryViewModel.showContent()
                        }
                    }
                }
            }
        }

        //Display the load state for Header and footer
        binding.listPatientHistory.adapter = patientHistoryAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter(),
            footer = LoadStateAdapter()
        )


        //Collecting the flow of paging data to display the patient sessions history list.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                patientHistoryViewModel.patientSessionsHistoryList.collectLatest {
                    patientHistoryAdapter.submitData(it)
                }
            }
        }



        // Add an Observer on the state variable for showing the snack bar when
        // we delete a session from the list.
        patientHistoryViewModel.showSnackBarEvent.observe(viewLifecycleOwner) { isShowingSnackBar ->
            if (isShowingSnackBar) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.patient_session_deletion),
                    Snackbar.LENGTH_LONG
                ).show()
                // Reset state to make sure the snackBar is only shown once, even if the device
                // has a configuration change.
                patientHistoryViewModel.doneShowingSnackBar()
            }

        }


    }
}