package com.example.android.clinicmanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.android.clinicmanagement.barChart.RoundedBarChart
import com.example.android.clinicmanagement.barChart.RoundedBarChartStyle
import com.github.mikephil.charting.data.BarEntry

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val entries = mutableListOf<BarEntry>()
        entries.add(BarEntry(0f, 3000f))
        entries.add(BarEntry(1f, 8150f))
        entries.add(BarEntry(2f, 9650f))
        entries.add(BarEntry(3f, 10500f))
//
        entries.add(BarEntry(4f, 8000f))
        entries.add(BarEntry(5f, 5000f))
        entries.add(BarEntry(6f, 5000f))
        val chart = findViewById<RoundedBarChart>(R.id.chart)
        val roundedBarChartStyle = RoundedBarChartStyle(this)
        roundedBarChartStyle.showChart(chart,entries,R.color.sunrise_orange)

        // TODO: the activity main is the main entry to the app change that
        val button = findViewById<Button>(R.id.button_switch)
        val image = findViewById<ImageView>(R.id.image_arrow_circle)
        val textChart = findViewById<TextView>(R.id.text_chart_title)
        button.setOnClickListener{
            image.background = ContextCompat.getDrawable(this,R.drawable.ic_arrowcircle_down)
            roundedBarChartStyle.showChart(chart,entries,R.color.cerulean)
            textChart.text = getString(R.string.title_chart_net_income_dh)
            it.background = ContextCompat.getDrawable(this,R.drawable.ic_switch_right)
        }
    }
}