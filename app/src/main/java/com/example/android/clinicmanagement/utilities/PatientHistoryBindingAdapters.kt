package com.example.android.clinicmanagement.utilities

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Session
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("sessionInfo")
fun TextView.setSessionInfo(session: Session?){
    val id = this.id
    if(session != null){
        this.text = with(session){
            when(id){
                R.id.text_date -> SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(dateInSeconds*1000)
                R.id.text_time -> SimpleDateFormat("HH:mm", Locale.getDefault()).format(dateInSeconds*1000)
                R.id.text_amount_payed -> context.getString(R.string.moroccan_currency_with_number,amountPayed)
                else -> throw java.lang.Exception("No sessions list item TextView Id matches")

            }
        }
    }
}