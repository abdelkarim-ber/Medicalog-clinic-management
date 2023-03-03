package com.example.android.clinicmanagement.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.android.clinicmanagement.expenditures.Expenditure
import com.example.android.clinicmanagement.expenditures.ExpenditureAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.barChart.RoundedBarChartStyle
import com.example.android.clinicmanagement.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.BarEntry
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialFadeThrough


class StatisticsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentStatisticsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_statistics, container, false
        )

        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, 3000f))
        entries.add(BarEntry(1f, 8150f))
        entries.add(BarEntry(2f, 9650f))
        entries.add(BarEntry(3f, 10500f))
//
        entries.add(BarEntry(4f, 8000f))
        entries.add(BarEntry(5f, 5000f))
        entries.add(BarEntry(6f, 5000f))
        val chart = binding.chart
        val roundedBarChartStyle = RoundedBarChartStyle(requireContext())
        roundedBarChartStyle.showChart(chart, entries, R.color.sunrise_orange)


        val button = binding.buttonSwitch
        val image = binding.imageArrowCircle
        val textChart = binding.textChartTitle
        button.setOnClickListener {
            image.setImageResource(R.drawable.ic_arrowcircle_down)
            roundedBarChartStyle.showChart(chart, entries, R.color.cerulean)
            textChart.text = getString(R.string.title_chart_net_income_dh)
            it.background = ContextCompat.getDrawable(requireContext(), R.drawable.ic_switch_right)
        }
        val adapter = ExpenditureAdapter()
        binding.listExpenditure.adapter = adapter
        adapter.data = listOf(
            Expenditure("Utilities", 2000),
            Expenditure("Rent", 400),
            Expenditure("Repair", 1000),
            Expenditure("Insurance", 250),
            Expenditure("Wages", 1000),
            Expenditure("Tax", 1200),
            Expenditure("Supplies", 1000),
            Expenditure("Divers", 100)
        )

        // Inflate the layout for this fragment
        return binding.root
    }

}