package com.rifqi.trackfunds.core.domain.savings.usecase

import java.math.BigDecimal

/**
 * Adds funds to a specific savings goal.
 */
interface AddFundsToSavingsGoalUseCase {
    suspend operator fun invoke(goalId: String, amount: BigDecimal): Result<Unit>
}