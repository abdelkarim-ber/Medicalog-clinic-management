package com.example.android.clinicmanagement.utilities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.expenses.ExpensesType
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("expensesHistoryInfo")
fun TextView.setExpensesHistoryInfo(expenditure: Expenditure?){
    val id = this.id
    if(expenditure != null){
        this.text = with(expenditure){
            when(id){
                R.id.text_date -> SimpleDateFormat("MMMM, yyyy", Locale.getDefault()).format(dateInSeconds*1000)
                R.id.title_expenses_type -> context.getString(ExpensesType.findExpensesTypeWithNumber(expendCategory)!!.stringResource)
                R.id.text_amount_payed -> context.getString(R.string.moroccan_currency_with_number,amountSpent)
                else -> throw Exception("No expenses history list item TextView Id matches")
            }
        }
    }
}

@BindingAdapter("expensesHistoryInfo")
fun View.setExpensesHistoryInfo(expenditure: Expenditure?){
     if(expenditure != null){
         val expensesType =  ExpensesType.findExpensesTypeWithNumber(expenditure.expendCategory)
         this.background.setTint(ContextCompat.getColor(context,expensesType!!.colorRes))
     }
 }
@BindingAdapter("expensesHistoryInfo")
fun ImageView.setExpensesHistoryInfo(expenditure: Expenditure?){
     if(expenditure != null){
         val expensesType =  ExpensesType.findExpensesTypeWithNumber(expenditure.expendCategory)
         this.setImageResource(expensesType!!.iconResource)
     }
 }