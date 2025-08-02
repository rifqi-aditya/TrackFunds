package com.rifqi.trackfunds.core.domain.budget.usecase

/**
 * Deletes a budget by its ID for the current user.
 */
interface DeleteBudgetUseCase {
    /**
     * @param budgetId The ID of the budget to delete.
     * @return A [Result] indicating success or failure.
     */
    suspend operator fun invoke(budgetId: String): Result<Unit>
}