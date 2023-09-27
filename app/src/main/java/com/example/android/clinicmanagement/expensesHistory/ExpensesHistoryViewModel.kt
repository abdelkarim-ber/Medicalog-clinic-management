package com.example.android.clinicmanagement.expensesHistory

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.utilities.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ExpensesHistoryViewModel(private val expenditureRepository: ExpenditureRepository) :
    ViewModel() {


    /**
     * Variable to set the current state of the screen to either show the actual content,
     * loading message or an empty state message when there is no content to show.
     */
    private val _expensesHistoryUIState: MutableLiveData<UiState> = MutableLiveData()

    /**
     * The immutable and exposed version of [_expensesHistoryUIState].
     */
    val expensesHistoryUIState: LiveData<UiState> = _expensesHistoryUIState


    /**
     * Variable that tells the fragment to show the snack bar.
     */
    private val _showSnackBarEvent = MutableLiveData<Boolean>()

    /**
     * The immutable and exposed version of [_showSnackBarEvent].
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackBarEvent


    /**
     * Variable to know if we're about to do a deletion operation
     * to let item change animations be displayed
     * instead of displaying a loading screen.
     * */
    var isDeletionUpdate:Boolean = false




    /**
     * Variable to set the current type of order chosen for expenses history list.
     */
    private val _expensesOrderType = MutableLiveData(ExpensesOrderType.DATE)

    /**
     * Variable to display the current type of order chosen as text.
     */
    val expensesOrderText = Transformations.map(_expensesOrderType){ orderType ->
        orderType.textResId
    }
    /**
     * Variable to load the expenses history list.
     */
    val expensesList: LiveData<PagingData<Expenditure>> = _expensesOrderType.switchMap { orderType ->
        expenditureRepository.loadExpensesOrderedBy(orderType).cachedIn(viewModelScope)
    }


    /**
     * Called to change the expenses history list order type.
     */
    fun changeExpensesHistoryOrderWith(expensesOrderType: ExpensesOrderType) {
        _expensesOrderType.value = expensesOrderType
    }


    /**
     * Called when we swipe an item from the list to delete it from the database.
     */
    fun deleteExpense(expenditure: Expenditure) {
        isDeletionUpdate = true
        viewModelScope.launch {
            expenditureRepository.deleteExpenditure(expenditure)
            _showSnackBarEvent.value = true
            //we delay for one second here to make sure that the deletion is performed &
            // that refresh state of the adapter is in Loading state before setting [isDeletionUpdate] back to false.
            delay(1000L)
            isDeletionUpdate = false
        }

    }

    /**
     * Called when we have no data to show to the user
     * to display the place holder screen.
     */
    fun showEmptyScreen() {
        _expensesHistoryUIState.value = UiState.Failure(
            R.string.expenses_history_empty_state_tagline,
            R.string.expenses_history_empty_state_message,
            R.drawable.img_placeholder_empty_wallet
        )
    }

    /**
     * Called when we actually have some data to show to the user.
     */
    fun showContent() {
        _expensesHistoryUIState.value = UiState.Success
    }

    /**
     * Called whenever we are in the process of loading data
     * so we can show the user a loading screen.
     */
    fun showLoadingScreen() {
        _expensesHistoryUIState.value = UiState.Loading(R.string.expenses_history_loading_message)
    }


    /**
     * Called to clear the request of showing the snack bar.
     */
    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }


}