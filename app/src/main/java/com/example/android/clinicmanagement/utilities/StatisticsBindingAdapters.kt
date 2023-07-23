package com.example.android.clinicmanagement.utilities

import android.content.Context
import android.util.Log
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
import com.example.android.clinicmanagement.statistics.ExpenditureListAdapter
import com.example.android.clinicmanagement.statistics.StatisticsScreenData
import com.github.mikephil.charting.data.BarEntry
import java.text.SimpleDateFormat
import java.util.*


enum class ChartType {
    NET_INCOME, EXPENDITURE
}

@BindingAdapter("showLoading")
fun View.showLoading(uiState: UiState?) {
    uiState?.let {
        if (uiState is UiState.Loading) {
            if (visibility == View.GONE) crossFadeIn()
        } else {
            if (visibility == View.VISIBLE) crossFadeOut()
        }
    }
}

@BindingAdapter("showPlaceHolder")
fun View.showPlaceHolder(uiState: UiState?) {
    uiState?.let {
        if (uiState is UiState.Failure) {
            if (visibility == View.GONE) scaleUp()
        } else {
            if (visibility == View.VISIBLE) crossFadeOut()
        }
    }
}

@BindingAdapter("showContent")
fun View.showContent(uiState: UiState?) {
    uiState?.let {
        if (uiState is UiState.Success<*>) {
            if (visibility == View.GONE)
                crossFadeIn()
        } else {
            if (visibility == View.VISIBLE)
                crossFadeOut()
        }
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
        text = context.getString(uiState.messageResource)
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

@BindingAdapter("chartTitleIcon")
fun ImageView.setChartTitleIcon(chartType: ChartType) {
    when (chartType) {
        ChartType.NET_INCOME -> this.setImageResource(R.drawable.ic_arrowcircle_down)
        ChartType.EXPENDITURE -> this.setImageResource(R.drawable.ic_arrowcircle_up)
    }
}

@BindingAdapter("chartTitle")
fun TextView.setChartTitle(chartType: ChartType) {
    when (chartType) {
        ChartType.NET_INCOME -> this.setText(R.string.title_chart_net_income_dh)
        ChartType.EXPENDITURE -> this.setText(R.string.title_chart_expenditure_dh)
    }
}


@BindingAdapter("expensesPercentage")
fun TextView.setExpensesPercentage(uiState: UiState) {
    if (uiState is UiState.Success<*>) {
        val statisticsData = (uiState.content as StatisticsScreenData)
        val income = statisticsData.income
        val expenses = statisticsData.expenses

        if (income != null && income != 0) {
            text = expenses?.let {
                if (income <= expenses) {
                    context.getString(R.string.statistics_spending_all)
                } else if (expenses == 0) {
                    context.getString(R.string.statistics_spending_nothing)
                } else {
                    val percentage = ((expenses.toFloat() / income) * 100).toInt()
                    context.getString(
                        R.string.statistics_spending_percentage,
                        percentage
                    )
                }
            } ?: context.getString(R.string.statistics_spending_nothing)
        } else {
            this.visibility = View.INVISIBLE
        }

    }
}

@BindingAdapter("incomeData")
fun TextView.setIncomeData(uiState: UiState) {
    if (uiState is UiState.Success<*>) {
        val statisticsData = (uiState.content as StatisticsScreenData)
        val income = statisticsData.income
        val incomeText = income ?: 0

        this.text = "$incomeText DH"
    }
}

@BindingAdapter("netIncomeData")
fun TextView.setNetIncomeData(uiState: UiState) {
    if (uiState is UiState.Success<*>) {
        val statisticsData = (uiState.content as StatisticsScreenData)
        val income = statisticsData.income
        val expenses = statisticsData.expenses

        val netIncome = income?.let {
            expenses?.let {
                if (income <= expenses) {
                    0
                } else {
                    income - expenses
                }
            } ?: income
        } ?: 0

        this.text = "$netIncome DH"
    }
}

@BindingAdapter("expensesData")
fun TextView.setExpensesData(uiState: UiState) {
    if (uiState is UiState.Success<*>) {
        val statisticsData = (uiState.content as StatisticsScreenData)
        val expenses = statisticsData.expenses
        val expensesText = expenses ?: 0

        this.text = "$expensesText DH"
    }
}


@BindingAdapter("uiState", "context", "chartType", "selectedMonth")
fun RoundedBarChart.setChartData(
    uiState: UiState,
    context: Context,
    chartType: ChartType,
    selectedMonth: Long
) {
    val loadedList: List<TotalByMonth>?
    val entries = mutableListOf<BarEntry>()
    val months = mutableListOf<String>()
    val inputFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    val outputFormat = SimpleDateFormat("MMM", Locale.getDefault())
    val selectedMonthFormatted = inputFormat.format(selectedMonth)


    val secondaryBarColor = when (chartType) {
        ChartType.NET_INCOME -> ContextCompat.getColor(context, R.color.cerulean)
        ChartType.EXPENDITURE -> ContextCompat.getColor(context, R.color.sunrise_orange)
    }
    val mainBarColor = when (chartType) {
        ChartType.NET_INCOME -> ContextCompat.getColor(context, R.color.green)
        ChartType.EXPENDITURE -> ContextCompat.getColor(context, R.color.yellow)
    }

    val chartColorList: IntArray

    var date: Date
    var monthFormatted: String

    if (uiState is UiState.Success<*>) {

        val statisticsData = (uiState.content as StatisticsScreenData)

        when (chartType) {
            ChartType.NET_INCOME -> {
                loadedList = statisticsData.netIncomeByMonthList
            }
            ChartType.EXPENDITURE -> {
                loadedList = statisticsData.expensesByMonthList
            }
        }


        if (loadedList != null && loadedList.size >= 4) {
            val indexMainBar =
                loadedList.indexOfFirst { item -> item.month == selectedMonthFormatted }
            chartColorList = IntArray(loadedList.size) { secondaryBarColor }

            if (indexMainBar != -1) {
                chartColorList[indexMainBar] = mainBarColor
            }

            loadedList.forEachIndexed { index, item ->
                entries.add(BarEntry(index.toFloat(), item.total.toFloat()))
                date = inputFormat.parse(item.month) as Date
                monthFormatted = outputFormat.format(date)
                months.add(monthFormatted)
            }

            val roundedBarChartBuilder = RoundedBarChartBuilder(context)
            roundedBarChartBuilder.buildChart(this, entries, months, chartColorList)

        } else {
            this.clear()
        }
    }

}

@BindingAdapter("expensesList","expensesTitle")
fun RecyclerView.setExpensesList(uiState: UiState,expensesTitle:View) {
    if (uiState is UiState.Success<*>) {
        val statisticsData = (uiState.content as StatisticsScreenData)
        val expensesByCategoryList = statisticsData.expensesByCategoryList
        val adapter = ExpenditureListAdapter()
        this.adapter = adapter

       if(expensesByCategoryList != null){
            adapter.data = expensesByCategoryList
            expensesTitle.visibility = View.VISIBLE
        } else {
           expensesTitle.visibility = View.GONE
       }

    }
}


/// binding adapters for expenditures by category list------------------------

@BindingAdapter("expensesBgColor")
fun View.setExpensesBgColor(totalSpentByCategory: TotalSpentByCategory) {
    val color =  when (totalSpentByCategory.expendCategory) {
            0 -> R.color.expenses_bright_pink_crayola
            1 -> R.color.expenses_coral
            2 -> R.color.expenses_blue_ncs
            3 -> R.color.expenses_emerald
            4 -> R.color.expenses_mantis
            5 -> R.color.expenses_light_sea_green
            6 -> R.color.expenses_midnight_green
            else -> R.color.expenses_sunglow
        }
    this.setBackgroundColor(ContextCompat.getColor(context,color))
}

@BindingAdapter("expensesTintColor")
fun ImageView.setExpensesTintColor(totalSpentByCategory: TotalSpentByCategory) {
    val color = when (totalSpentByCategory.expendCategory) {
        0 -> R.color.expenses_bright_pink_crayola
        1 -> R.color.expenses_coral
        2 -> R.color.expenses_blue_ncs
        3 -> R.color.expenses_emerald
        4 -> R.color.expenses_mantis
        5 -> R.color.expenses_light_sea_green
        6 -> R.color.expenses_midnight_green
        else -> R.color.expenses_sunglow
    }
    this.setColorFilter(ContextCompat.getColor(context,color))
}
@BindingAdapter("expensesTintColor")
fun View.setExpensesTintColor(totalSpentByCategory: TotalSpentByCategory) {
    val color = when (totalSpentByCategory.expendCategory) {
        0 -> R.color.expenses_bright_pink_crayola
        1 -> R.color.expenses_coral
        2 -> R.color.expenses_blue_ncs
        3 -> R.color.expenses_emerald
        4 -> R.color.expenses_mantis
        5 -> R.color.expenses_light_sea_green
        6 -> R.color.expenses_midnight_green
        else -> R.color.expenses_sunglow
    }
    this.background.setTint(ContextCompat.getColor(context,color))
}

@BindingAdapter("expensesCategoryIcon")
fun ImageView.setExpensesCategoryIcon(totalSpentByCategory: TotalSpentByCategory) {
    val icon = when (totalSpentByCategory.expendCategory) {
        0    -> R.drawable.ic_expenditure_receipt
        1    -> R.drawable.ic_expenditure_home
        2    -> R.drawable.ic_expenditure_home_repair
        3    -> R.drawable.ic_expenditure_insurance
        4    -> R.drawable.ic_expenditure_wallet
        5    -> R.drawable.ic_expenditure_tax
        6    -> R.drawable.ic_expenditure_medical_supplies
        else -> R.drawable.ic_expenditure_divers
    }
    this.setImageResource(icon)
}

@BindingAdapter("expensesCategoryName")
fun TextView.setExpensesCategoryName(totalSpentByCategory: TotalSpentByCategory) {
    this.text = when (totalSpentByCategory.expendCategory) {
        0    -> context.getString(R.string.utilities)
        1    -> context.getString(R.string.rent)
        2    -> context.getString(R.string.repair)
        3    -> context.getString(R.string.insurance)
        4    -> context.getString(R.string.wages)
        5    -> context.getString(R.string.tax)
        6    -> context.getString(R.string.supplies)
        else -> context.getString(R.string.divers)
    }

}
