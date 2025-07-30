package com.rifqi.trackfunds.core.domain.usecase.budget

import com.rifqi.trackfunds.core.domain.model.Budget

/**
 * Updates an existing budget for the current user.
 */
interface UpdateBudgetUseCase {
    /**
     * @param budget The updated budget item.
     * @return A [Result] indicating success or failure.
     */
    suspend operator fun invoke(budget: Budget): Result<Unit>
}