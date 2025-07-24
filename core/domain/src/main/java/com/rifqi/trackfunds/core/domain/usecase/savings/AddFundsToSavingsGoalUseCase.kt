package com.rifqi.trackfunds.core.domain.usecase.savings

import java.math.BigDecimal

/**
 * Adds funds to a specific savings goal.
 */
interface AddFundsToSavingsGoalUseCase {
    suspend operator fun invoke(goalId: String, amount: BigDecimal): Result<Unit>
}