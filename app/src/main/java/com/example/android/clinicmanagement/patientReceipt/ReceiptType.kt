package com.example.android.clinicmanagement.patientReceipt

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.example.android.clinicmanagement.R

enum class ReceiptType(val number: Int, @StringRes val stringResource: Int,@ColorRes val titleColor:Int) {
    QUOTATION(1, R.string.quotation,R.color.cerulean),INVOICE(2,R.string.invoice,R.color.sunrise_orange);

    companion object {
        /**
         * Returns the corresponding [ReceiptType] based on the passed number.
         *@param number the number that identifies the receipt type.
         */
        fun findReceiptTypeWithNumber(number: Int): ReceiptType? {
            return values().find { it.number == number }
        }
    }
}