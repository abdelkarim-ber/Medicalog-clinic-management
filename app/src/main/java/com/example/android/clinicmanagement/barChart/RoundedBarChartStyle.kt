package com.example.android.clinicmanagement.barChart

import android.content.Context
import android.graphics.Typeface
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.android.clinicmanagement.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlin.math.roundToInt


class RoundedBarChartStyle(val context: Context) {
    private fun styleChart(barChart: BarChart) {
        val typeFace: Typeface? = ResourcesCompat.getFont(context, R.font.comfortaa_regular)

        barChart.apply {
            axisRight.isEnabled = false
            xAxis.apply {
                isGranularityEnabled = true
                granularity = 1f
                setDrawAxisLine(true)
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                typeface = typeFace
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return formatter(value)
                    }
                }
                //setCenterAxisLabels(true)
            }
            axisLeft.apply {
                // isGranularityEnabled = true
                // granularity = 2f
                isEnabled = true
                setDrawAxisLine(true)
                setDrawGridLines(true)
                typeface = typeFace
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.roundToInt()} DH"
                    }
                }
            }
            setDrawBarShadow(true)
            setFitBars(true)// make the x-axis fit exactly all bars
            setTouchEnabled(false)
            isDragEnabled = false
            setScaleEnabled(false)
            setPinchZoom(false)
            description = null
            legend.isEnabled = false
        }
    }

    private fun styleBarDataSet(barDataSet: BarDataSet,barColor:Int) {
        barDataSet.apply {
            color = ContextCompat.getColor(context,barColor)
            setDrawValues(false)
            isHighlightEnabled = false
            barShadowColor = ContextCompat.getColor(context, R.color.color_primary_10)
        }
    }

    fun showChart(barChart: BarChart, entries: MutableList<BarEntry>,barColor:Int) {
        val barDataSet: BarDataSet = BarDataSet(entries, "BarDataSet")
        val barData: BarData = BarData(barDataSet)
        styleChart(barChart)
        styleBarDataSet(barDataSet,barColor)
        barData.setBarWidth(0.7f); // set custom bar width
        barChart.setData(barData);
        barChart.invalidate();
    }

    fun formatter(date: Float) = when (date) {
        0f -> "Mon"
        1f -> "Sat"
        2f -> "Wed"
        else -> "Tue"
    }


}
