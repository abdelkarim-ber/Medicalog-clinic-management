package com.example.android.clinicmanagement.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.TotalSpentByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenditureDao {
    @Insert
    suspend fun insert(expenditure: Expenditure)

    @Delete
    suspend fun delete(expenditure: Expenditure)

    @Query("SELECT * FROM expenditure_table "+
            "ORDER BY " +
            " CASE :order" +
            " WHEN 0 THEN date_in_seconds" +
            " WHEN 1 THEN id" +
            " END Desc"
    )
    fun loadExpensesOrderedBy(order:Int): PagingSource<Int, Expenditure>





    @Query(
        "SELECT expenditure_category,sum(amount_spent) AS total FROM expenditure_table " +
                "WHERE strftime('%Y-%m', date_in_seconds, 'unixepoch') = :month " +
                "GROUP BY expenditure_category"
    )
    fun loadExpensesByCategoryForMonth(month: String): Flow<Array<TotalSpentByCategory>?>

    @Query("SELECT sum(amount_spent) FROM expenditure_table "+
            "WHERE strftime('%Y-%m', date_in_seconds, 'unixepoch') = :month "
    )
    fun getExpensesForMonth(month: String):Flow<Int?>

    @Query(
        "SELECT strftime('%Y-%m', date_in_seconds, 'unixepoch') AS month ,sum(amount_spent) AS total FROM expenditure_table " +
                "WHERE month BETWEEN strftime('%Y-%m',:month||'-01','-3 month') AND strftime('%Y-%m',:month||'-01','+3 month')  " +
                "GROUP BY month"
    )
    fun loadExpenditureStatsForMonthAndNeighbors(month: String): Flow<Array<TotalByMonth>?>
    //Month argument must be in the format  "YYYY-MM"
}