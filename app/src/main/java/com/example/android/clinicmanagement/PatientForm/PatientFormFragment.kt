package com.example.android.clinicmanagement.PatientForm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientFormBinding
import com.example.android.clinicmanagement.databinding.FragmentReceiptBinding


class PatientFormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPatientFormBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_patient_form, container, false
        )

        val items = listOf("Male", "Female")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_gender, items)
        (binding.layoutGender.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}