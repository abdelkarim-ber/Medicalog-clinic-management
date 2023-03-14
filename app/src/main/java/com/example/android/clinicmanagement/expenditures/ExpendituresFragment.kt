package com.example.android.clinicmanagement.expenditures

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentExpendituresBinding
import com.example.android.clinicmanagement.statistics.StatisticsFragmentDirections
import com.google.android.material.datepicker.MaterialDatePicker


class ExpendituresFragment : Fragment() {

    lateinit var binding: FragmentExpendituresBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_expenditures, container, false
        )

        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolBar.setNavigationOnClickListener { findNavController().navigateUp() }

        val manager = GridLayoutManager(activity, 3)
        binding.listExpenditureCategory.layoutManager = manager

        val adapter = ExpenditureCategoryAdapter()
        binding.listExpenditureCategory.adapter = adapter

        binding.buttonSave.setOnClickListener {
            if(ExpenditureCategoryAdapter.ViewHolder.selectedItem != null && binding.textFieldAmount.text.toString() != ""  ) {
                Toast.makeText(
                    requireContext(),
                    "Item clicked is: ${ExpenditureCategoryAdapter.ViewHolder.selectedItem}" +
                            " Amount is :${binding.textFieldAmount.text}",
                    Toast.LENGTH_LONG
                ).show()
            }else
                Toast.makeText(
                    requireContext(),
                    "field is empty or item not selected",
                    Toast.LENGTH_LONG
                ).show()
        }

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .setSelection(MaterialDatePicker.thisMonthInUtcMilliseconds())
                .build()

        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.calendar -> datePicker.show(childFragmentManager, "tag")
            }
            true
        }
    }

    override fun onDestroy() {
        ExpenditureCategoryAdapter.ViewHolder.clean()
        Log.v("recyclerview","fragment destroyed")
        super.onDestroy()
    }
}