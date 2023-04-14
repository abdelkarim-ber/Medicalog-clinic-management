package com.example.android.clinicmanagement.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.Expenditure
import com.example.android.clinicmanagement.data.models.TotalByCategory
import com.example.android.clinicmanagement.data.models.TotalByMonth

@Dao
interface ExpenditureDao {
    @Insert
    suspend fun insert(expenditure: Expenditure)

    @Delete
    suspend fun delete(expenditure: Expenditure)

    @Query(
        "SELECT expenditure_category,sum(amount_spent) AS total FROM expenditure_table " +
                "WHERE strftime('%Y-%m', date_in_seconds, 'unixepoch') = :month " +
                "GROUP BY expenditure_category"
    )
    suspend fun loadTotalByCategoryForMonth(month: String): Array<TotalByCategory>


    @Query(
        "SELECT strftime('%Y-%m', date_in_seconds, 'unixepoch') AS month ,sum(amount_spent) AS total FROM expenditure_table " +
                "WHERE month BETWEEN strftime('%Y-%m',:month||'-01','-3 month') AND strftime('%Y-%m',:month||'-01','+3 month')  " +
                "GROUP BY month"
    )
    suspend fun loadExpenditureStatsForMonthAndNeighbors(month: String): Array<TotalByMonth>
    //Month argument must be in the format  "YYYY-MM"
}