package com.example.android.clinicmanagement.statistics

import android.app.Application
import androidx.lifecycle.*
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.TotalByMonth
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.data.repositories.SessionsRepository
import com.example.android.clinicmanagement.utilities.ChartType
import com.example.android.clinicmanagement.utilities.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StatisticsViewModel(
    private val sessionsRepository: SessionsRepository,
    private val expenditureRepository: ExpenditureRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _statisticsUIState: MutableLiveData<UiState> = MutableLiveData()

    val statisticsUIState: LiveData<UiState> = _statisticsUIState

    private val _chartType: MutableLiveData<ChartType> = MutableLiveData(ChartType.NET_INCOME)

    val chartType: LiveData<ChartType> = _chartType


    private val monthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    private val monthDisplayedFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())

    private val _selectedDateInMillis: MutableLiveData<Long> = MutableLiveData()
    val selectedDateInMillis: LiveData<Long> = _selectedDateInMillis

    val selectedDateFormatted = Transformations.map(selectedDateInMillis) {
        monthDisplayedFormat.format(it)
    }


    init {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        val previousMonth = monthFormat.format(calendar.time)
        _selectedDateInMillis.value = calendar.timeInMillis
        loadStatisticsForMonth(previousMonth)

    }

    private fun loadStatisticsForMonth(
        month: String
    ) {
        viewModelScope.launch {
            _statisticsUIState.value = UiState.Loading(R.string.statistics_loading_message)
            delay(3000L)

            val income = sessionsRepository.getIncomeForMonth(month)
            val expenses = expenditureRepository.getExpensesForMonth(month)

            if ((income != null && income != 0) || (expenses != null && expenses != 0)) {

                val incomeByMonthList =
                    sessionsRepository.loadIncomeStatsForMonthAndNeighbors(month)
                val expensesByMonthList =
                    expenditureRepository.loadExpenditureStatsForMonthAndNeighbors(month)

                val netIncomeByMonth = incomeByMonthList?.map { incomeByMonth ->
                    //find expenses with the same month
                    val expensesByMonth = expensesByMonthList?.find { expensesByMonth ->
                        expensesByMonth.month == incomeByMonth.month
                    }
                    val netIncome = expensesByMonth?.let {
                        if (incomeByMonth.total <= expensesByMonth.total) {
                            0
                        } else {
                            incomeByMonth.total - expensesByMonth.total
                        }

                    } ?: incomeByMonth.total


                    TotalByMonth(
                        incomeByMonth.month,
                        netIncome
                    )
                }

                val expensesByCategoryList =
                    expenditureRepository.loadTotalByCategoryForMonth(month)

                val statisticsScreenData = StatisticsScreenData(
                    income,
                    expenses,
                    netIncomeByMonth,
                    expensesByMonthList?.toList(),
                    expensesByCategoryList?.toList()
                )

                _statisticsUIState.value = UiState.Success(statisticsScreenData)

            } else {
                _statisticsUIState.value = UiState.Failure(
                    R.string.statistics_empty_state_tagline,
                    R.string.statistics_empty_state_message,
                    R.drawable.img_placeholder_data_processing
                    )

            }
        }
    }

    fun switchChart() {
        val currentChartType = _chartType.value
        when (currentChartType) {
            ChartType.NET_INCOME -> _chartType.value = ChartType.EXPENDITURE
            else -> _chartType.value = ChartType.NET_INCOME
        }
    }

    fun setSelectedMonth(dateInMillis: Long) {
        _selectedDateInMillis.value = dateInMillis
        loadStatisticsForMonth(monthFormat.format(dateInMillis))
    }


}