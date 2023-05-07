package com.example.android.clinicmanagement.barChart

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.android.clinicmanagement.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter


class RoundedBarChartBuilder(val context: Context) {

    private fun styleBarDataSet(barDataSet: BarDataSet, barColors: IntArray) {
        barDataSet.apply {
            colors = barColors.toList()
            setDrawValues(false)
            isHighlightEnabled = false
            barShadowColor = ContextCompat.getColor(context, R.color.color_primary_10)
        }
    }

    fun buildChart(
        barChart: BarChart,
        entries: List<BarEntry>,
        XAxisMonths: List<String>,
        barColors: IntArray
    ) {

        barChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return XAxisMonths[value.toInt()]
            }
        }
        barChart.animateY(1500, Easing.EaseOutBack);
        val barDataSet: BarDataSet = BarDataSet(entries, "BarDataSet")
        val barData: BarData = BarData(barDataSet)
        styleBarDataSet(barDataSet, barColors)
        barData.setBarWidth(0.7f); // set custom bar width
        barChart.setData(barData);
        barChart.invalidate();
    }

}
