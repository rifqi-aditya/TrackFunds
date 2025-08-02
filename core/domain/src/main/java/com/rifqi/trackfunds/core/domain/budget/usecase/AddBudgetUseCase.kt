package com.rifqi.trackfunds.core.domain.budget.usecase

import com.rifqi.trackfunds.core.domain.budget.model.Budget

/**
 * Adds a new budget for the current user.
 */
interface AddBudgetUseCase {
    /**
     * @param budget The budget to add.
     * @return A [Result] indicating success or failure.
     */
    suspend operator fun invoke(budget: Budget): Result<Unit>
}
