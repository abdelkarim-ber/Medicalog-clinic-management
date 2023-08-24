package com.example.android.clinicmanagement.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.clinicmanagement.data.datasources.ExpenditureDataSource
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth
import com.example.android.clinicmanagement.expensesHistory.ExpensesHistoryViewModel.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val ITEMS_PER_PAGE = 10
private const val MAX_ITEMS = 50

class ExpenditureRepository(private val expenditureDataSource: ExpenditureDataSource) {
    suspend fun addExpenditure(expenditure: Expenditure) =
        expenditureDataSource.addExpenditure(expenditure)

    suspend fun deleteExpenditure(expenditure: Expenditure) =
        expenditureDataSource.deleteExpenditure(expenditure)

    fun loadExpensesOrderedBy(orderType: OrderType): Flow<PagingData<Expenditure>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = ITEMS_PER_PAGE * 2,
                maxSize = MAX_ITEMS,
                pageSize = ITEMS_PER_PAGE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                expenditureDataSource.loadExpensesOrderedBy(orderType.index)

            }
        ).flow

    }


    fun loadExpensesByCategoryForMonth(month: String): Flow<List<TotalSpentByCategory>?> =
        expenditureDataSource.loadExpensesByCategoryForMonth(month).map { it?.toList() }

    fun getExpensesForMonth(month: String):Flow<Int?> =
        expenditureDataSource.getExpensesForMonth(month)

    fun loadExpenditureStatsForMonthAndNeighbors(month: String): Flow<List<TotalByMonth>?> =
        expenditureDataSource.loadExpenditureStatsForMonthAndNeighbors(month).map { it?.toList() }
}