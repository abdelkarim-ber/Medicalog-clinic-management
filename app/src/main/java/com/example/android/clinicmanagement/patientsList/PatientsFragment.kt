package com.example.android.clinicmanagement.patientsList

import android.os.Bundle

import android.view.*
import android.widget.FrameLayout
import androidx.core.util.Pair
import androidx.core.view.doOnPreDraw
import androidx.core.view.marginBottom

import androidx.fragment.app.Fragment

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

import com.example.android.clinicmanagement.R

import com.example.android.clinicmanagement.databinding.FragmentPatientsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialElevationScale
import java.text.SimpleDateFormat
import java.util.*

import kotlin.math.roundToInt

class PatientsFragment : Fragment() {

    lateinit var binding: FragmentPatientsBinding
    lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patients, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.rangeSlider.setLabelFormatter { value ->
            "${value.roundToInt()} ${getString(R.string.years_old_abbreviation)}"
        }
        binding.rangeSlider.addOnChangeListener { _, _, _ ->
            val values = binding.rangeSlider.values
            binding.titleRange.text =
                getString(R.string.title_range, values[0].roundToInt(), values[1].roundToInt())
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.isDraggable = false
        enableFilterWidgets(false)

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()


        binding.textLayoutDate.setStartIconOnClickListener {
            // Respond to end icon presses
            dateRangePicker.show(childFragmentManager, "tag")

        }

        dateRangePicker.addOnPositiveButtonClickListener {
            var formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date1 = Date(it.first)
            val date2 = Date(it.second)
            var formattedDate = "${formatter.format(date1)} to ${formatter.format(date2)}"
            binding.textFieldDate.setText(formattedDate)

        }


        binding.appBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter -> setBottomSheetVisibility(it)
            }
            // Handle the menu selection
            true
        }

        val viewModelFactory = PatientsViewModelFactory()

        val patientsViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PatientsViewModel::class.java)

        binding.listPatients.adapter = PatientsAdapter { view, id ->
            patientsViewModel.onPatientClicked(view, id)
        }

        binding.viewModel = patientsViewModel

        patientsViewModel.navigateToNewPatient.observe(viewLifecycleOwner) { fabInfo ->
            val fabView = fabInfo.first
            val fabClicked = fabInfo.second
            val layoutParams = fabView?.layoutParams as? ViewGroup.MarginLayoutParams
            layoutParams?.bottomMargin =
                resources.getDimensionPixelSize(com.google.android.material.R.dimen.design_bottom_navigation_height) +
                        resources.getDimensionPixelSize(R.dimen.margin_16dp)
            fabView?.layoutParams = layoutParams

            if (fabClicked && fabView != null) {
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
                val newPatientFormTransitionName =
                    getString(R.string.new_patient_form_transition_name)
                val extras = FragmentNavigatorExtras(fabView to newPatientFormTransitionName)

                this.findNavController()
                    .navigate(PatientsFragmentDirections.actionPatientsToPatientForm(), extras)
                patientsViewModel.onNewPatientNavigated()
            }
        }



        patientsViewModel.navigateToPatientProfile.observe(
            viewLifecycleOwner
        )
        { info ->
            //info parameter consist of view that's clicked and patient id
            info?.let {
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
                val patientProfileTransitionName =
                    getString(R.string.patient_profile_transition_name)
                val extras = FragmentNavigatorExtras(info.first to patientProfileTransitionName)

                this.findNavController()
                    .navigate(
                        PatientsFragmentDirections.actionPatientsToPatientProfile(info.second),
                        extras
                    )
                patientsViewModel.onPatientProfileNavigated()
            }

        }


    }

    private fun setBottomSheetVisibility(menuItem: MenuItem) {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            menuItem.setIcon(R.drawable.ic_filter)
            enableFilterWidgets(false)
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            menuItem.setIcon(R.drawable.ic_close)
            enableFilterWidgets(true)

        }
    }

    private fun enableFilterWidgets(isEnabled: Boolean) {
        binding.textFieldFirstName.isEnabled = isEnabled
        binding.textFieldLastName.isEnabled = isEnabled
        binding.textFieldDate.isEnabled = isEnabled
        binding.rangeSlider.isEnabled = isEnabled
    }
}