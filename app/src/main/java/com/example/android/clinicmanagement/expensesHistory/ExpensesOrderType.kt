package com.example.android.clinicmanagement.expensesHistory

import androidx.annotation.StringRes
import com.example.android.clinicmanagement.R

/**
 * Enum class where we define a set of order types the expenses history list can be ordered by.
 * @param index the number that identifies the Order type.
 * @param textResId the string resource id for text related to the order type.
 */
enum class ExpensesOrderType(val index: Int, @StringRes val textResId: Int) {
    DATE(0, R.string.date), RECENTLY_ADDED(1, R.string.recently_added);

    companion object {
        /**
         * Returns the corresponding [ExpensesOrderType] based on the passed index.
         *@param index the number that identifies the Order type.
         */
        fun findOrderTypeWithIndex(index: Int): ExpensesOrderType? {
            return values().find { it.index == index }
        }
    }

}