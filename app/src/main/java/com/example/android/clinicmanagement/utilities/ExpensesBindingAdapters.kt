package com.example.android.clinicmanagement.utilities

import android.view.View
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.expenses.ExpensesRegistrationEvent
import com.example.android.clinicmanagement.expenses.ExpensesViewModel

import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("OnExpensesAmountChanged")
fun TextInputEditText.setOnExpensesAmountChanged(viewModel: ExpensesViewModel) {
    this.doOnTextChanged { text, start, before, count ->
        if (text != null) {
            viewModel.onEvent(ExpensesRegistrationEvent.AmountPayedChanged(text.toString()))
        }
    }
}

@BindingAdapter("expensesIcon")
fun ImageView.setExpensesIcon(iconResource: Int) {
    this.setImageResource(iconResource)
}

@BindingAdapter("showProgressIndic")
fun View.showProgressIndic(isShown: Boolean) {
    if (isShown) {
        if (visibility == View.GONE) crossFadeIn()
    } else {
        if (visibility == View.VISIBLE) crossFadeOut()
    }
}




