package com.example.android.clinicmanagement.expenses

import android.app.Application
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.domain.ValidateTextUseCase
import com.github.mikephil.charting.utils.Utils.init
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ExpensesViewModel(
    private val expenditureRepository: ExpenditureRepository,
    private val validateTextUseCase: ValidateTextUseCase = ValidateTextUseCase()
) : ViewModel() {
    /**
     * Variable that tells the fragment to change the previously clicked
     * expenses type card to its default style.
     */
    private val _previousCardSelected = MutableLiveData<Int?>(null)

    /**
     * the immutable and exposed version of [_previousCardSelected]
     */
    val previousCardSelected: LiveData<Int?>
        get() = _previousCardSelected

    /**
     * Variable that tells the fragment to change the clicked
     * expenses type card to a different style than its default one.
     */
    private val _currentCardSelected = MutableLiveData<Int?>(null)

    /**
     * the immutable and exposed version of [_currentCardSelected]
     */
    val currentCardSelected: LiveData<Int?>
        get() = _currentCardSelected

    /**
     * Variable that tells whether to show the circular progress indicator or not.
     */
    private val _showCircularProgress: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * the immutable and exposed version of [_showCircularProgress]
     */
    val showCircularProgress: LiveData<Boolean> = _showCircularProgress

    /**
     * Variable that store the values of the textInputEditTexts
     * and the states of the error values of those textInputEditTexts.
     */
    var expensesDataState = ExpensesDataState()

    private val monthYearFormat = SimpleDateFormat("MMMM, yyyy", Locale.getDefault())

    /**
     * Variable that store the selected month and year in form of date in milliseconds.
     */
    private val _selectedDateInMillis: MutableLiveData<Long> = MutableLiveData()

    /**
     * Each time we set a value on [_selectedDateInMillis] this variable value gets updated.
     */
    val selectedDateFormatted = Transformations.map(_selectedDateInMillis) {
        monthYearFormat.format(it)
    }

    /**
     * Variable that tells the fragment to show the snack bar.
     */
    private val _showSnackBarEvent = MutableLiveData<Boolean>()

    /**
     * The immutable and exposed version of [_showSnackBarEvent].
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent



    init {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -1)
        //To avoid gaps for ex when it is the month of february,
        // also in this date/time in milliseconds we only consider the month and year.
        calendar.set(Calendar.DAY_OF_MONTH, 10)
        _selectedDateInMillis.value = calendar.timeInMillis
    }

    /**
     * Called whenever we made a change in the textInputEditTexts
     * to save the changes each time in [expensesDataState].
     */
    fun onEvent(event: ExpensesRegistrationEvent) {
        expensesDataState = when (event) {
            is ExpensesRegistrationEvent.ExpensesTypeChanged -> expensesDataState.copy(
                expensesNumber = event.expensesNumber
            )
            is ExpensesRegistrationEvent.AmountPayedChanged -> expensesDataState.copy(amountPayed = event.amountPayed)
        }
    }

    /**
     * Called when we click on an expenses type card.
     */
    fun onCardSelected(position: Int, expensesNumber: Int) {
        _previousCardSelected.value = _currentCardSelected.value
        _currentCardSelected.value = position
        onEvent(ExpensesRegistrationEvent.ExpensesTypeChanged(expensesNumber))
    }

    /**
     * Called when we click the save button to persist the entered data after
     * we check that it is valid.
     */
    fun submit() {

        val amountPayedResult = validateTextUseCase(expensesDataState.amountPayed)
        val isExpensesTypeSelected = expensesDataState.expensesNumber != -1

        if (!isExpensesTypeSelected || !amountPayedResult.isSuccessful) {
            expensesDataState.errorAmountPayed?.value = amountPayedResult.errorMessage
            expensesDataState.errorExpensesNumber?.value = !isExpensesTypeSelected
        } else {
            //remove the error indicators
            expensesDataState.errorAmountPayed?.value = null
            expensesDataState.errorExpensesNumber?.value = false

            viewModelScope.launch {
                // To show the circular progress indicator
                _showCircularProgress.value = true

                delay(700L)

                val expenditure =
                    Expenditure(
                        dateInSeconds = _selectedDateInMillis.value!! / 1000,
                        expendCategory = expensesDataState.expensesNumber,
                        amountSpent = expensesDataState.amountPayed.toInt()
                    )
                expenditureRepository.addExpenditure(expenditure)

                // To hide the circular progress indicator
                _showCircularProgress.value = false
                _showSnackBarEvent.value = true

            }
        }

    }

    /**
     * Called when we choose the month and date from the monthPicker dialog.
     */
    fun setSelectedMonthAndYear(dateInMillis: Long) {
        _selectedDateInMillis.value = dateInMillis
    }

    /**
     * Called to reset the expenses type selection.
     */
    fun clearExpensesTypeSelection() {
        _previousCardSelected.value = _currentCardSelected.value
        expensesDataState = expensesDataState.copy(expensesNumber = -1)
    }


    /**
     * Called to clear the request of showing the snack bar.
     */
    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }



}