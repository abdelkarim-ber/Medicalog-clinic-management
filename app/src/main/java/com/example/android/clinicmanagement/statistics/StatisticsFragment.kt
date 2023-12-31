package com.example.android.clinicmanagement.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.ClinicApplication
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.databinding.FragmentStatisticsBinding
import com.example.android.clinicmanagement.monthPicker.MonthPickerDialog


class StatisticsFragment : Fragment() {

    lateinit var binding: FragmentStatisticsBinding
    private lateinit var statisticsViewModel: StatisticsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val application = requireNotNull(this.activity).application

        val appContainer = (application as ClinicApplication).appContainer

        val statisticsViewModelFactory = StatisticsViewModelFactory(
            appContainer.sessionsRepository,
            appContainer.expenditureRepository
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

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }


        binding.listExpenditure.adapter = ExpenditureListAdapter()

        val expenditureListAdapter = binding.listExpenditure.adapter

        expenditureListAdapter?.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(
                positionStart: Int,
                itemCount: Int
            ) {
                binding.listExpenditure.scrollToPosition(0)
            }

            override fun onItemRangeRemoved(
                positionStart: Int,
                itemCount: Int
            ) {
                binding.listExpenditure.smoothScrollToPosition(0)
            }
        })

        val monthPicker = MonthPickerDialog()
        monthPicker.addOnPositiveButtonClickListener { dateInSeconds ->
            statisticsViewModel.loadStatsForMonth(dateInSeconds * 1000)
        }

        binding.toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.calendar -> monthPicker.showMonthPicker(childFragmentManager, "tag")
            }
            true
        }

    }
}