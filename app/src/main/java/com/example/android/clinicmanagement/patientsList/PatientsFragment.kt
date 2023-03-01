package com.example.android.clinicmanagement.patientsList

import android.icu.util.UniversalTimeScale.toLong
import android.os.Bundle

import android.view.*
import android.widget.FrameLayout

import androidx.fragment.app.Fragment

import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController

import com.example.android.clinicmanagement.R

import com.example.android.clinicmanagement.databinding.FragmentPatientsBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

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

        val viewModelFactory = PatientsViewModelFactory()

        val patientsViewModel =
            ViewModelProvider(
                this, viewModelFactory
            ).get(PatientsViewModel::class.java)

        binding.listPatients.adapter = PatientsAdapter { view,id ->
            patientsViewModel.onPatientClicked(view,id)
        }

        patientsViewModel.navigateToPatientProfile.observe(
            viewLifecycleOwner)
            { info ->
                //info parameter consist of view that's clicked and patient id
                info?.let {
                    this.findNavController()
                        .navigate(PatientsFragmentDirections.actionPatientsToPatientProfile(info.second))
                    patientsViewModel.onPatientProfileNavigated()
                }

            }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.appBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.filter -> setBottomSheetVisibility(it)
            }
            // Handle the menu selection
            true
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