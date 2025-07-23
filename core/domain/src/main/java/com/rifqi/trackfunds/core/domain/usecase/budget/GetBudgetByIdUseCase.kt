package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem

/**
 * Gets a single budget by its ID for the current user.
 */
interface GetBudgetByIdUseCase {
    /**
     * @param budgetId The ID of the budget to fetch.
     * @return A [Result] containing the [BudgetItem] on success or an error on failure.
     */
    suspend operator fun invoke(budgetId: String): Result<BudgetItem>
}