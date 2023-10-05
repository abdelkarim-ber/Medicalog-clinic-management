package com.example.android.clinicmanagement.expenses

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.android.clinicmanagement.R


enum class ExpensesType(val number: Int, @StringRes val stringResource: Int, @DrawableRes val iconResource:Int,@ColorRes val colorRes: Int) {
    UTILITIES(1, R.string.utilities, R.drawable.ic_expenditure_services,R.color.expenses_imperial_red),
    RENT(2, R.string.rent, R.drawable.ic_expenditure_home,R.color.expenses_orange_crayola),
    REPAIR(3, R.string.repair, R.drawable.ic_expenditure_home_repair,R.color.expenses_coral),
    INSURANCE(4, R.string.insurance, R.drawable.ic_expenditure_insurance,R.color.expenses_midnight_green),
    WAGES(5, R.string.wages, R.drawable.ic_expenditure_wallet,R.color.expenses_pistachio),
    TAXES(6, R.string.taxes, R.drawable.ic_expenditure_taxes,R.color.expenses_zomp),
    SUPPLIES(7, R.string.supplies, R.drawable.ic_expenditure_medical_supplies,R.color.expenses_egyptian_blue),
    OTHER(8, R.string.other, R.drawable.ic_expenditure_other,R.color.expenses_persian_indigo);

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