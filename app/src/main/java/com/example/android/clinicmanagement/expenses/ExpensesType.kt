package com.example.android.clinicmanagement.expenses

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.android.clinicmanagement.R


enum class ExpensesType(val number: Int, @StringRes val stringResource: Int, @DrawableRes val iconResource:Int,@ColorRes val colorRes: Int) {
    UTILITIES(1, R.string.utilities, R.drawable.ic_expenditure_receipt,R.color.expenses_bright_pink_crayola),
    RENT(2, R.string.rent, R.drawable.ic_expenditure_home,R.color.expenses_coral),
    REPAIR(3, R.string.repair, R.drawable.ic_expenditure_home_repair,R.color.expenses_blue_ncs),
    INSURANCE(4, R.string.insurance, R.drawable.ic_expenditure_insurance,R.color.expenses_emerald),
    WAGES(5, R.string.wages, R.drawable.ic_expenditure_wallet,R.color.expenses_mantis),
    TAX(6, R.string.tax, R.drawable.ic_expenditure_tax,R.color.expenses_light_sea_green),
    SUPPLIES(7, R.string.supplies, R.drawable.ic_expenditure_medical_supplies,R.color.expenses_midnight_green),
    DIVERS(8, R.string.divers, R.drawable.ic_expenditure_divers,R.color.expenses_sunglow);

    companion object {
        /**
         * Returns the corresponding [ExpensesType] based on the passed number.
         *@param number the number that identifies the expenses Type.
         */
        fun findExpensesTypeWithNumber(number: Int): ExpensesType? {
            return values().find { it.number == number }
        }
    }
}