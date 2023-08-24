package com.example.android.clinicmanagement.domain

import com.example.android.clinicmanagement.data.repositories.ExpenditureRepository
import com.example.android.clinicmanagement.data.repositories.SessionsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine


class GetNetIncomeUseCase(private val sessionsRepository: SessionsRepository,
                          private val expenditureRepository: ExpenditureRepository
) {
    /**
     * @param month must be in format "YYYY-MM".
     */
    operator fun invoke(month:String): Flow<Int?> {
        val incomeFlow = sessionsRepository.getIncomeForMonth(month)
        val expensesFlow = expenditureRepository.getExpensesForMonth(month)
        return combine(incomeFlow, expensesFlow){ income,expenses ->

            income?.let {
                expenses?.let {
                    if (income <= expenses) {
                        0
                    } else {
                        income - expenses
                    }
                } ?: income
            }

        }

    }
}