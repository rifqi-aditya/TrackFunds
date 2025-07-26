package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.BudgetModel

/**
 * Adds a new budget for the current user.
 */
interface AddBudgetUseCase {
    /**
     * @param budgetModel The budget to add.
     * @return A [Result] indicating success or failure.
     */
    suspend operator fun invoke(budgetModel: BudgetModel): Result<Unit>
}
