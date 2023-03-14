package com.example.android.clinicmanagement.patientForm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.fragment.findNavController
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientFormBinding
import com.google.android.material.color.MaterialColors
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform


class PatientFormFragment : Fragment() {

    lateinit var binding: FragmentPatientFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            setAllContainerColors(ContextCompat.getColor(requireContext(),R.color.white))
            setPathMotion(MaterialArcMotion())
            duration = resources.getInteger(R.integer.clinicmanagement_motion_duration_medium).toLong()
            interpolator = FastOutSlowInInterpolator()
            fadeMode = MaterialContainerTransform.FADE_MODE_IN
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patient_form, container, false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val items = listOf("Male", "Female")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_gender, items)
        (binding.layoutGender.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

    }
}