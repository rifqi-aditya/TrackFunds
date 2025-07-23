package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetItem

/**
 * Adds a new budget for the current user.
 */
interface AddBudgetUseCase {
    /**
     * @param budgetItem The budget to add.
     * @return A [Result] indicating success or failure.
     */
    suspend operator fun invoke(budgetItem: BudgetItem): Result<Unit>
}
