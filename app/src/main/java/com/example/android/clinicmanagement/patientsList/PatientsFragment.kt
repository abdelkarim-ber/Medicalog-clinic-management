package com.example.android.clinicmanagement.patientsList

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.util.Pair
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialElevationScale
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class PatientsFragment : Fragment() {

    private lateinit var binding: FragmentPatientsBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var patientsViewModel: PatientsViewModel

    private lateinit var resetMenuItem: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val patientsViewModelFactory = PatientsViewModelFactory(appContainer.patientRepository)

        patientsViewModel = ViewModelProvider(
            this, patientsViewModelFactory
        ).get(PatientsViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patients, container, false
        )
        binding.viewModel = patientsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.dialog_date_range_picker_title))
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        dateRangePicker.addOnPositiveButtonClickListener {
            var formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date1 = Date(it.first)
            val date2 = Date(it.second)
            var formattedDate = "${formatter.format(date1)} → ${formatter.format(date2)}"
            binding.textFilterConsultationDateRange.setText(formattedDate)

        }


        with(binding) {

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehavior.isDraggable = false
            enableFilterWidgets(false)

            layoutConsultationDateRange.setStartIconOnClickListener {
                // Respond to end icon presses
                dateRangePicker.show(childFragmentManager, "tag")
            }




            resetMenuItem = appBar.menu.findItem(R.id.reset)


            resetMenuItem.setOnMenuItemClickListener {
                resetFilter()
                true
            }




            ageRangeSlider.setLabelFormatter { value ->
                "${value.roundToInt()} ${getString(R.string.years_old_abbreviation)}"
            }

            appBar.setNavigationOnClickListener {
                setBottomSheetVisibility()
            }


            //to reset Age range slider
            buttonResetAge.setOnClickListener {
                resetAge()
            }


            //To prevent the keyboard from showing when focusing on Consultation date range field
            textFilterConsultationDateRange.showSoftInputOnFocus = false
            //to hide keyboard when focusing on when focusing on Consultation date range field
            textFilterConsultationDateRange.setOnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    val inputMethodManager =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }

        }


        val patientsListAdapter = PatientsAdapter { view, id ->
            patientsViewModel.onPatientClicked(view, id)
        }

        //Display the load state for the initial patients list data load
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                patientsListAdapter.loadStateFlow.collect { loadStates ->
                    //we check if it's the initial load
                    if( loadStates.refresh is LoadState.Loading){
                        patientsViewModel.showLoadingScreen()
                        //Here we delay for a second to give the animations of showing/hiding loading
                        // and showing/hiding content enough time to run.
                        delay(1000L)
                        //After each refresh we scroll to position zero either when we change filter infos
                        // or when there is a change in the underlying data of the current patients list.
                        binding.listPatients.scrollToPosition(0)
                    }

                    //We check if the list has finished loading
                    //Then corresponding to the list item count we either show the place holder screen
                    // or the actual content.
                    if (loadStates.refresh is LoadState.NotLoading) {
                        if (patientsListAdapter.itemCount == 0) {
                            patientsViewModel.showEmptyScreen()
                        } else {
                            patientsViewModel.showContent()
                        }
                    }


                }
            }
        }

        //Display the load state for Header and footer
        binding.listPatients.adapter = patientsListAdapter.withLoadStateHeaderAndFooter(
            header = LoadStateAdapter(),
            footer = LoadStateAdapter()
        )

        // Add an Observer for the patients list when a new list
        // with a new set of filter informations is requested.
        patientsViewModel.patientsList.observe(viewLifecycleOwner) { patientsList ->
            patientsListAdapter.submitData(lifecycle,patientsList)
        }


        // Add an Observer on the state variable for Navigating to patient form screen when
        // the fab button is clicked
        patientsViewModel.navigateToNewPatientEvent.observe(viewLifecycleOwner) { fabInfo ->
            val fabView = fabInfo.first
            val fabClicked = fabInfo.second

            //To keep the same float action button position when the bottom navigation view is removed
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


        // Add an Observer on the state variable for Navigating to patient profile screen when
        // a patients list item is clicked
        patientsViewModel.navigateToPatientProfileEvent.observe(
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


    /**
     * Executes when we click the filter button
     */
    private fun setBottomSheetVisibility() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            //Only when the bottom sheet is up ,only then we load patients list data
            patientsViewModel.loadPatients()

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.appBar.setNavigationIcon(R.drawable.ic_filter)
            resetMenuItem.isVisible = false
            enableFilterWidgets(false)

        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            binding.appBar.setNavigationIcon(R.drawable.ic_close)
            resetMenuItem.isVisible = true
            enableFilterWidgets(true)

        }
    }

    /**
     * Enable/Disable all widgets of the back layer
     */
    private fun enableFilterWidgets(isEnabled: Boolean) {
        with(binding) {
            textFilterFirstName.isEnabled = isEnabled
            textFilterLastName.isEnabled = isEnabled
            textFilterConsultationDateRange.isEnabled = isEnabled
            textFilterDiagnosis.isEnabled = isEnabled
            chipGroupGender.apply {
                getChildAt(0).isEnabled = isEnabled
                getChildAt(1).isEnabled = isEnabled
            }
            chipGroupSessionsCompletion.apply {
                getChildAt(0).isEnabled = isEnabled
                getChildAt(1).isEnabled = isEnabled
            }
            ageRangeSlider.isEnabled = isEnabled
            buttonResetAge.isEnabled = isEnabled
        }
    }

    /**
     * Resets age range slider to its initial state
     */
    private fun resetAge() {
        val values = resources.getIntArray(R.array.initial_range_slider_values)
        binding.titleAgeRange.text =
            getString(R.string.title_age_range, values[0], values[1])
        binding.ageRangeSlider.setValues(values[0].toFloat(), values[1].toFloat())
    }

    /**
     * Resets all back layer widgets to their initial state
     */
    private fun resetFilter(){
        with(binding){
            textFilterFirstName.text?.clear()
            textFilterLastName.text?.clear()
            textFilterConsultationDateRange.text?.clear()
            textFilterDiagnosis.text?.clear()
            chipGroupGender.clearCheck()
            chipGroupSessionsCompletion.clearCheck()
            resetAge()
        }
    }



}