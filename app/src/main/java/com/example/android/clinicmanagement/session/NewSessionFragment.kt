package com.example.android.clinicmanagement.session

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentNewSessionBinding
import com.example.android.clinicmanagement.patientProfile.PatientProfileFragmentArgs
import com.example.android.clinicmanagement.patientsList.PatientsViewModel
import com.example.android.clinicmanagement.patientsList.PatientsViewModelFactory
import com.example.android.clinicmanagement.utilities.crossFadeIn
import com.example.android.clinicmanagement.utilities.crossFadeOut
import com.example.android.clinicmanagement.utilities.scaleDown
import com.example.android.clinicmanagement.utilities.scaleUp
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class NewSessionFragment : BottomSheetDialogFragment() {
    private val args: NewSessionFragmentArgs by navArgs()
    private lateinit var binding: FragmentNewSessionBinding
    private lateinit var newSessionViewModel: NewSessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val newSessionViewModelFactory =
            NewSessionViewModelFactory(args.patientKey, appContainer.sessionsRepository)

        newSessionViewModel = ViewModelProvider(
            this, newSessionViewModelFactory
        ).get(NewSessionViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_new_session, container, false
        )
        binding.viewModel = newSessionViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }


        //Here we want the dialog fragment to be expanded initially.
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
        }
        //Here we set the color to transparent to keep the rounded corners of the root view even in the expanded state.
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)

        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText(getString(R.string.dialog_time_picker_title))
                .build()

        timePicker.addOnPositiveButtonClickListener {
            binding.textTime.setText("${timePicker.hour}:${timePicker.minute}")
        }

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.dialog_date_picker_title))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val date = Date(it)
            var formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            var formattedDate = formatter.format(date)
            binding.textDate.setText(formattedDate)
        }


        binding.layoutTime.setStartIconOnClickListener {
            // Respond to end icon presses
            timePicker.show(childFragmentManager, "tag1")
        }

        binding.layoutDate.setStartIconOnClickListener {
            // Respond to end icon presses
            datePicker.show(childFragmentManager, "tag2")
        }
        // Add an Observer on the state variable for whether to show the circular indicator,
        // or hide it and dismiss the dialog.
        newSessionViewModel.showCircularProgress.observe(viewLifecycleOwner) { isProgressIndicVisible ->
            with(binding) {
                if (isProgressIndicVisible) {
                    progressCircular.crossFadeIn()
                } else {
                    viewLifecycleOwner.lifecycleScope.launch {
                        //textFilterFirstName
                        progressCircular.crossFadeOut()
                        delay(700L)
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            getString(R.string.new_session_add),
                            Snackbar.LENGTH_LONG
                        ).show()
                        dismiss()
                    }

                }
            }
        }

    }
}