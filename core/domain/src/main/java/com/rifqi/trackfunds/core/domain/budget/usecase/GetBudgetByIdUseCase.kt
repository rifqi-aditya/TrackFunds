package com.rifqi.trackfunds.core.domain.budget.usecase

import com.rifqi.trackfunds.core.domain.budget.model.Budget

/**
 * Gets a single budget by its ID for the current user.
 */
interface GetBudgetByIdUseCase {
    /**
     * @param budgetId The ID of the budget to fetch.
     * @return A [Result] containing the [Budget] on success or an error on failure.
     */
    suspend operator fun invoke(budgetId: String): Result<Budget>
}