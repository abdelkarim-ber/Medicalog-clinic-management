package com.example.android.clinicmanagement.utilities


import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.barChart.RoundedBarChart
import com.example.android.clinicmanagement.barChart.RoundedBarChartBuilder
import com.example.android.clinicmanagement.data.models.TotalByMonth
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.expenses.ExpensesType
import com.example.android.clinicmanagement.statistics.ChartType
import com.example.android.clinicmanagement.statistics.ExpenditureListAdapter
import com.github.mikephil.charting.data.BarEntry
import java.text.SimpleDateFormat
import java.util.*




@BindingAdapter("showLoading")
fun View.showLoading(uiState: UiState?) {

    if (uiState is UiState.Loading) {
        if (visibility == View.GONE) crossFadeIn()
    } else {
        if (visibility == View.VISIBLE) crossFadeOut()
    }

}

@BindingAdapter("showPlaceHolder")
fun View.showPlaceHolder(uiState: UiState?) {

    if (uiState is UiState.Failure) {
        if (visibility == View.GONE) scaleUp()
    } else {
        if (visibility == View.VISIBLE) crossFadeOut()
    }

}

@BindingAdapter("showContent")
fun View.showContent(uiState: UiState?) {

    if (uiState is UiState.Success) {
        if (visibility == View.GONE)
            crossFadeIn()
    } else {
        if (visibility == View.VISIBLE)
            crossFadeOut()
    }

}

//Binding Adapters for empty State Screen items
@BindingAdapter("emptyStateIcon")
fun ImageView.setEmptyStateIcon(uiState: UiState?) {
    if (uiState is UiState.Failure) {
        this.setImageResource(uiState.imageDrawableRes)
    }
}

@BindingAdapter("tagLineText")
fun TextView.setTagLineText(uiState: UiState?) {
    if (uiState is UiState.Failure) {
        text = context.getString(uiState.tagLineResource)
    }
}

@BindingAdapter("messageText")
fun TextView.setMessageText(uiState: UiState?) {
    if (uiState is UiState.Failure) {

        text = uiState.formatArg?.let { argument ->

            context.getString(uiState.messageResource, argument)

        } ?: context.getString(uiState.messageResource)

    }
}

//binding adapter for  loading screen
@BindingAdapter("loadingMessageText")
fun TextView.setLoadingMessageText(uiState: UiState?) {
    if (uiState is UiState.Loading) {
        text = context.getString(uiState.messageResource)
    }
}
//----
@BindingAdapter("switchButtonImage")
fun ImageButton.setSwitchButtonImage(chartType: ChartType) {
    when (chartType) {
        ChartType.NET_INCOME -> this.setImageResource(R.drawable.ic_switch_right)
        ChartType.EXPENDITURE -> this.setImageResource(R.drawable.ic_switch_left)
    }
}

@BindingAdapter("chartTypeIcon")
fun ImageView.setChartTypeIcon(chartType: ChartType) {
    when (chartType) {
        ChartType.NET_INCOME -> this.setImageResource(R.drawable.ic_arrowcircle_down)
        ChartType.EXPENDITURE -> this.setImageResource(R.drawable.ic_arrowcircle_up)
    }
}

@BindingAdapter("chartTitle")
fun TextView.setChartTitle(chartType: ChartType) {
    when (chartType) {
        ChartType.NET_INCOME -> this.setText(R.string.title_chart_net_income)
        ChartType.EXPENDITURE -> this.setText(R.string.title_chart_expenses)
    }
}


@BindingAdapter("incomeOperand", "expensesOperand")
fun TextView.setExpensesPercentage(incomeOperand: Int?, expensesOperand: Int?) {

    text = if (incomeOperand != null && incomeOperand != 0) {
         expensesOperand?.let {
            if (incomeOperand <= expensesOperand) {
                context.getString(R.string.statistics_spending_all)
            } else if (expensesOperand == 0) {
                context.getString(R.string.statistics_spending_nothing)
            } else {
                val percentage = ((expensesOperand.toFloat() / incomeOperand) * 100).toInt()
                context.getString(
                    R.string.statistics_spending_percentage,
                    percentage
                )
            }
        } ?: context.getString(R.string.statistics_spending_nothing)
    } else {
        ""
    }


}

@BindingAdapter("incomeData")
fun TextView.setIncomeData(income: Int?) {
    this.text = context.getString(R.string.moroccan_currency_with_number, income ?: 0)
}

@BindingAdapter("netIncomeData")
fun TextView.setNetIncomeData(netIncome: Int?) {
    this.text = context.getString(R.string.moroccan_currency_with_number, netIncome ?: 0)
}

@BindingAdapter("expensesData")
fun TextView.setExpensesData(expenses: Int?) {
    this.text = context.getString(R.string.moroccan_currency_with_number, expenses ?: 0)
}


@BindingAdapter("netIncomeStatsList", "expensesStatsList", "chartType", "selectedMonthForQuery")
fun RoundedBarChart.setChartData(
    netIncomeStatsList: List<TotalByMonth>?,
    expensesStatsList: List<TotalByMonth>?,
    chartType: ChartType,
    selectedMonthForQuery: String?
) {
    val entries = mutableListOf<BarEntry>()
    val months = mutableListOf<String>()
    val yearMonthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    val shortMonthFormat = SimpleDateFormat("MMM", Locale.getDefault())


    val mainBarColor = when (chartType) {
        ChartType.NET_INCOME -> ContextCompat.getColor(context, R.color.cerulean)
        ChartType.EXPENDITURE -> ContextCompat.getColor(context, R.color.sunrise_orange)
    }
    val secondaryBarColor = when (chartType) {
        ChartType.NET_INCOME -> ContextCompat.getColor(context, R.color.green)
        ChartType.EXPENDITURE -> ContextCompat.getColor(context, R.color.yellow)
    }

    val loadedList: List<TotalByMonth>? = when (chartType) {
        ChartType.NET_INCOME -> {
            netIncomeStatsList
        }
        ChartType.EXPENDITURE -> {
            expensesStatsList
        }
    }


    val chartColorList: IntArray

    var date: Date



    if (loadedList != null && loadedList.size >= 3) {

        chartColorList = IntArray(loadedList.size) { mainBarColor }

        if (selectedMonthForQuery != null) {
            val indexMainBar =
                loadedList.indexOfFirst { item -> item.month == selectedMonthForQuery }
            if (indexMainBar != -1) {
                chartColorList[indexMainBar] = secondaryBarColor
            }
        }

        loadedList.forEachIndexed { index, item ->
            entries.add(BarEntry(index.toFloat(), item.total.toFloat()))
            date = yearMonthFormat.parse(item.month) as Date
            months.add(shortMonthFormat.format(date).replaceFirstChar { it.uppercase(Locale.ROOT) })
        }

        val roundedBarChartBuilder = RoundedBarChartBuilder(context)
        roundedBarChartBuilder.buildChart(this, entries, months, chartColorList)

    } else {
        this.clear()
    }


}

@BindingAdapter("expensesByCategoryList")
fun RecyclerView.setExpensesList(expensesByCategoryList: List<TotalSpentByCategory>?) {

    if (expensesByCategoryList != null ) {
        val adapter = ExpenditureListAdapter()
        this.adapter = adapter
        adapter.data = expensesByCategoryList
    }

}
@BindingAdapter("expensesListTitleVisibility")
fun View.setExpensesListTitleVisibility(expensesByCategoryList: List<TotalSpentByCategory>?) {
    if (expensesByCategoryList != null && expensesByCategoryList.isNotEmpty()) {
       this.visibility = View.VISIBLE
    }else{
        this.visibility = View.GONE
    }

}


/// binding adapters for expenditures by category list------------------------

@BindingAdapter("expensesTintColor")
fun View.setExpensesTintColor(totalSpentByCategory: TotalSpentByCategory) {
    val expensesType = ExpensesType.findExpensesTypeWithNumber(totalSpentByCategory.expendCategory)
    this.background.setTint(ContextCompat.getColor(context, expensesType!!.colorRes))
}

@BindingAdapter("expensesCategoryIcon")
fun ImageView.setExpensesCategoryIcon(totalSpentByCategory: TotalSpentByCategory) {
    val expensesType = ExpensesType.findExpensesTypeWithNumber(totalSpentByCategory.expendCategory)
    this.setImageResource(expensesType!!.iconResource)
}

@BindingAdapter("expensesCategoryName")
fun TextView.setExpensesCategoryName(totalSpentByCategory: TotalSpentByCategory) {
    val expensesType = ExpensesType.findExpensesTypeWithNumber(totalSpentByCategory.expendCategory)
    this.text = context.getString(expensesType!!.stringResource)
}
