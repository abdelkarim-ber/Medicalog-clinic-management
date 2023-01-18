package com.example.android.clinicmanagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.android.clinicmanagement.barChart.RoundedBarChart
import com.example.android.clinicmanagement.barChart.RoundedBarChartStyle
import com.example.android.clinicmanagement.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.BarEntry


class StatisticsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding:FragmentStatisticsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_statistics, container, false)

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
        roundedBarChartStyle.showChart(chart,entries,R.color.sunrise_orange)

        // TODO: the activity main is the main entry to the app change that
        val button = binding.buttonSwitch
        val image = binding.imageArrowCircle
        val textChart = binding.textChartTitle
        button.setOnClickListener{
            image.background = ContextCompat.getDrawable(requireContext(),R.drawable.ic_arrowcircle_down)
            roundedBarChartStyle.showChart(chart,entries,R.color.cerulean)
            textChart.text = getString(R.string.title_chart_net_income_dh)
            it.background = ContextCompat.getDrawable(requireContext(),R.drawable.ic_switch_right)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}