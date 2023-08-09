package com.example.android.clinicmanagement.expensesHistory

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.android.clinicmanagement.R
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.utilities.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ExpensesHistoryViewModel(private val expenditureRepository: ExpenditureRepository) :
    ViewModel() {

    /**
     * Variable that tells the adapter to collect from a new pager
     * in case we change the expenses list's order type .
     */
    private val _expensesListPagerEvent = MutableLiveData<Flow<PagingData<Expenditure>>>()
    /**
     * The immutable and exposed version of [_expensesListPagerEvent].
     */
    val expensesListPagerEvent: LiveData<Flow<PagingData<Expenditure>>> = _expensesListPagerEvent




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
     * Variable to display the current type of order chosen.
     */
    private val _orderText = MutableLiveData<Int>()
    /**
     * The immutable and exposed version of [_orderText].
     */
    val orderText: LiveData<Int>
            get()= _orderText


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




    init {
        loadExpensesListWithOrder(OrderType.DATE)
    }


    /**
     * Called to load the list of expenses ordered by the type passed as an argument.
     * @param orderType the type of order the expenses list is ordered by.
     */
    fun loadExpensesListWithOrder(orderType: OrderType) {
        //text to be displayed in order title section.
        _orderText.value = orderType.textResId
        _expensesListPagerEvent.value = expenditureRepository.loadExpensesOrderedBy(orderType)
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
        _expensesHistoryUIState.value = UiState.Success(null)
    }

    /**
     * Called whenever we are in the process of loading data
     * so we can show the user a loading screen.
     */
    fun showLoadingScreen() {
        if(_expensesHistoryUIState.value !is UiState.Failure)
            _expensesHistoryUIState.value = UiState.Loading(R.string.expenses_history_loading_message)
    }


    /**
     * Called to clear the request of showing the snack bar.
     */
    fun doneShowingSnackBar() {
        _showSnackBarEvent.value = false
    }


    /**
     * Enum class where we define a set of order types the expenses list can be ordered by.
     * @param index the number that identifies the Order type.
     * @param textResId the string resource id for text related to the order type.
     */
    enum class OrderType(val index: Int, @StringRes val textResId: Int) {
        DATE(0, R.string.date), RECENTLY_ADDED(1, R.string.recently_added);

        companion object {
            /**
             * Returns the corresponding [OrderType] based on the passed index.
             *@param index the number that identifies the Order type.
             */
            fun findOrderTypeWithIndex(index: Int): OrderType? {
                return values().find { it.index == index }
            }
        }

    }
}