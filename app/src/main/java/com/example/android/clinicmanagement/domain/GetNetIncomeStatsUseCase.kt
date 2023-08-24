package com.example.android.clinicmanagement.domain

import com.example.android.clinicmanagement.data.models.TotalByMonth
import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.data.repositories.SessionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class GetNetIncomeStatsUseCase(
    private val sessionsRepository: SessionsRepository,
    private val expenditureRepository: ExpenditureRepository
) {
    operator fun invoke(month: String):Flow<List<TotalByMonth>?> {

        val incomeStatsFlow = sessionsRepository.loadIncomeStatsForMonthAndNeighbors(month)
        val expensesStatsFlow =
            expenditureRepository.loadExpenditureStatsForMonthAndNeighbors(month)

       return combine(incomeStatsFlow, expensesStatsFlow) { incomeStatsArray, expensesStatsArray ->
            incomeStatsArray?.map { incomeByMonth ->
                //find expenses with the same month
                val expensesByMonth = expensesStatsArray?.find { expensesByMonth ->
                    expensesByMonth.month == incomeByMonth.month
                }
                val netIncomeByMonthTotal = expensesByMonth?.let {
                    if (incomeByMonth.total <= expensesByMonth.total) {
                        0
                    } else {
                        incomeByMonth.total - expensesByMonth.total
                    }

                } ?: incomeByMonth.total

                TotalByMonth(
                    incomeByMonth.month,
                    netIncomeByMonthTotal
                )
            }

        }


    }
//
}