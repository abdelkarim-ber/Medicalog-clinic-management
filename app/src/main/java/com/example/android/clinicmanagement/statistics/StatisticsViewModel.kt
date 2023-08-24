package com.example.android.clinicmanagement.statistics

import androidx.lifecycle.*
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.data.repositories.SessionsRepository
import com.example.android.clinicmanagement.domain.GetNetIncomeStatsUseCase
import com.example.android.clinicmanagement.domain.GetNetIncomeUseCase
import com.example.android.clinicmanagement.utilities.ChartType
import com.example.android.clinicmanagement.utilities.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class StatisticsViewModel(
    private val sessionsRepository: SessionsRepository,
    private val expenditureRepository: ExpenditureRepository,
    private val getNetIncomeUseCase: GetNetIncomeUseCase=GetNetIncomeUseCase(sessionsRepository,expenditureRepository),
    private val getNetIncomeStatsUseCase: GetNetIncomeStatsUseCase=GetNetIncomeStatsUseCase(sessionsRepository,expenditureRepository)
) : ViewModel() {
    /**
     * Variable to set the current state of the screen to either show the actual content,
     * loading message or an empty state message when there is no content to show.
     */
    private val _statisticsUIState: MutableLiveData<UiState> = MutableLiveData()
    /**
     * The immutable and exposed version of [_statisticsUIState].
     */
    val statisticsUIState: LiveData<UiState> = _statisticsUIState

    /**
     * Variable to set what type of chart (Net Income/Expenses stats) to show to the user.
     */
    private val _chartType: MutableLiveData<ChartType> = MutableLiveData(ChartType.NET_INCOME)
    /**
     * The immutable and exposed version of [_chartType].
     */
    val chartType: LiveData<ChartType> = _chartType

    /**
     * Variable that formats the selected date to a format needed to query the data
     * from the database.
     */
    private val queryMonthFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())

    /**
     * Variable that formats the selected date to a convenient format to display on the screen.
     */
    private val screenMonthFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())


    /**
     * Variable to set the month and year in form of date in milliseconds.
     */
    private val _selectedDateInMillis: MutableLiveData<Long> = MutableLiveData()


    val selectedMonthForScreen = Transformations.map(_selectedDateInMillis) {
        screenMonthFormat.format(it)
    }
    val selectedMonthForQuery = Transformations.map(_selectedDateInMillis) {
        queryMonthFormat.format(it)
    }

    //Each time we change the [selectedMonthForQuery] value, the following
    //variables get updated with a new observable data retrieved from the database.

    val income = selectedMonthForQuery.switchMap {
        sessionsRepository.getIncomeForMonth(it).asLiveData()
    }
    val netIncome = selectedMonthForQuery.switchMap {
        getNetIncomeUseCase(it).asLiveData()
    }
    val netIncomeStatsList = selectedMonthForQuery.switchMap {
        getNetIncomeStatsUseCase(it).asLiveData()
    }

    val expenses = selectedMonthForQuery.switchMap {
        expenditureRepository.getExpensesForMonth(it).asLiveData()
    }
    val expensesStatsList = selectedMonthForQuery.switchMap {
        expenditureRepository.loadExpenditureStatsForMonthAndNeighbors(it).asLiveData()
    }
    val expensesByCategoryList = selectedMonthForQuery.switchMap {
        expenditureRepository.loadExpensesByCategoryForMonth(it).asLiveData()
    }



    init {
        //On launch we request the data of the previous month.

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        //To avoid gaps for ex when it is the month of february
        calendar.set(Calendar.DAY_OF_MONTH, 10)
        loadStatsForMonth(calendar.timeInMillis)

    }


    /**
     * Called when we click on the switch button to change the type of chart.
     */
    fun switchChart() {
        when (_chartType.value) {
            ChartType.NET_INCOME -> _chartType.value = ChartType.EXPENDITURE
            else -> _chartType.value = ChartType.NET_INCOME
        }
    }

    /**
     * Called to request the data related to the selected month and year.
     */
    fun loadStatsForMonth(dateInMillis: Long) {
        viewModelScope.launch {
            _statisticsUIState.value = UiState.Loading(R.string.statistics_loading_message)
            delay(700L)
            val isDataAvailable = sessionsRepository.isDataAvailableForMonth(queryMonthFormat.format(dateInMillis))
            if (isDataAvailable){
                _selectedDateInMillis.value = dateInMillis
                //To give time for data to be loaded.
                delay(1000L)
                _statisticsUIState.value = UiState.Success

            }else{
                _statisticsUIState.value = UiState.Failure(
                    R.string.statistics_empty_state_tagline,
                    R.string.statistics_empty_state_message,
                    R.drawable.img_placeholder_empty_wallet,
                    screenMonthFormat.format(dateInMillis)
                )
            }
        }


    }


}