package com.example.android.clinicmanagement.receipt

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentPatientHistoryBinding
import com.example.android.clinicmanagement.databinding.FragmentReceiptBinding
import com.example.android.clinicmanagement.patientHistory.PatientHistoryAdapter

class ReceiptFragment : Fragment() {

lateinit var binding: FragmentReceiptBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_receipt, container, false
        )
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}