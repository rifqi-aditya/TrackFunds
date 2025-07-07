package com.rifqi.trackfunds.core.domain.usecase.savings

import java.math.BigDecimal

interface AddFundsToSavingsGoalUseCase {
    suspend operator fun invoke(goalId: String, amount: BigDecimal)
}