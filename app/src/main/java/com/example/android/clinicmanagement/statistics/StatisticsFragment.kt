package com.example.android.clinicmanagement.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentStatisticsBinding
import com.example.android.clinicmanagement.monthPicker.MonthPickerDialog
import java.text.SimpleDateFormat
import java.util.*


class StatisticsFragment : Fragment() {

    lateinit var binding: FragmentStatisticsBinding
    private lateinit var statisticsViewModel: StatisticsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val statisticsViewModelFactory = StatisticsViewModelFactory(
            appContainer.sessionsRepository,
            appContainer.expenditureRepository,
            application
        )

        val viewModel = ViewModelProvider(
            this, statisticsViewModelFactory
        ).get(StatisticsViewModel::class.java)

        statisticsViewModel = viewModel

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_statistics, container, false
        )
        binding.viewModel = statisticsViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val monthPicker = MonthPickerDialog()
        monthPicker.addOnPositiveButtonClickListener { dateInSeconds ->
            statisticsViewModel.setSelectedMonth(dateInSeconds * 1000)
        }

        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.calendar -> monthPicker.showMonthPicker(childFragmentManager, "tag")
                R.id.addExpenditure -> findNavController().navigate(StatisticsFragmentDirections.actionStatisticsToExpenses())
            }
            true
        }

    }
}