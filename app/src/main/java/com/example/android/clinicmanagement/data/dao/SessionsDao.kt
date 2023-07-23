package com.example.android.clinicmanagement.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.android.clinicmanagement.data.models.Session
import com.example.android.clinicmanagement.data.models.TotalByMonth


@Dao
interface SessionsDao {
    @Insert
    suspend fun insert(session: Session)

    @Delete
    suspend fun delete(session: Session)

    @Query("SELECT * FROM session_table "+
            "WHERE patient_id = :patientId " +
            "ORDER BY date_in_seconds DESC"
    )
    fun loadSessionsWithPatientID(patientId:Long): PagingSource<Int,Session>

    @Query("SELECT count(*) FROM session_table "+
            "WHERE patient_id = :patientId "
    )
    suspend fun countSessionsWithPatientID(patientId:Long):Int

    @Query("SELECT sum(amount_payed) FROM session_table "+
            "WHERE patient_id = :patientId "
    )
    suspend fun getAmountPayedByPatientWithID(patientId: Long):Int

    @Query("SELECT sum(amount_payed) FROM session_table "+
            "WHERE strftime('%Y-%m', date_in_seconds, 'unixepoch') = :month "
    )
    suspend fun getIncomeForMonth(month: String):Int?


    @Query("SELECT strftime('%Y-%m', date_in_seconds, 'unixepoch') AS month ,sum(amount_payed) AS total FROM session_table "+
            "WHERE month BETWEEN strftime('%Y-%m',:month||'-01','-3 month') AND strftime('%Y-%m',:month||'-01','+3 month')  "+
            "GROUP BY month"
    )
    suspend fun loadIncomeStatsForMonthAndNeighbors(month: String): Array<TotalByMonth>?
    //Month argument must be in the format  "YYYY-MM"
}