package com.example.android.clinicmanagement.expenditures

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentExpendituresBinding


class ExpendituresFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentExpendituresBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_expenditures, container, false
        )

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
        // Inflate the layout for this fragment
        return binding.root
    }


}