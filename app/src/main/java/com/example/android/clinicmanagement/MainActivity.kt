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
    }
}