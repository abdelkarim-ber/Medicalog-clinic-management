package com.example.android.clinicmanagement.utilities

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Receipt
import com.example.android.clinicmanagement.patientReceipt.ReceiptType
import java.util.*

@BindingAdapter("receiptDetails")
fun TextView.setReceiptDetails(receipt: Receipt?) {
    val id = this.id
    if (receipt != null) {
        this.text = with(receipt) {
            when (id) {
                R.id.text_receipt_number -> getReceiptNumberInYear(
                    receiptNumber,
                    receiptDateInSeconds
                )

                R.id.text_receipt_date -> getReceiptFormattedDate(receiptDateInSeconds)

                R.id.text_patient_full_name -> getFormattedFullName(firstName,lastName)

                R.id.text_session_count -> resources.getQuantityString(
                    R.plurals.done_sessions_field,
                    sessionCount,
                    sessionCount
                )

                R.id.text_unit_price -> context.getString(
                    R.string.moroccan_currency_with_number,
                    sessionPrice
                )

                R.id.text_total -> context.getString(
                    R.string.moroccan_currency_with_number,
                    total
                )
                else -> throw java.lang.Exception("No Patient receipt TextView Id matches")

            }
        }
    }
}

@BindingAdapter("receiptTitle")
fun TextView.setReceiptTitle(receiptType: ReceiptType) {
    this.text = context.getString(receiptType.stringResource)
    this.setTextColor(ContextCompat.getColor(context,receiptType.titleColor))
}

@BindingAdapter("receiptSessionCount")
fun TextView.setReceiptSessionCount(receiptType: ReceiptType) {
    val text = when(receiptType){
        ReceiptType.QUOTATION->R.string.planned_sessions
        ReceiptType.INVOICE->R.string.done_sessions
    }
    this.text = context.getString(text)
}

