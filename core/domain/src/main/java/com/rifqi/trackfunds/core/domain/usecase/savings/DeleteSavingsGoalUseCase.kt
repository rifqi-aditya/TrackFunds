package com.rifqi.trackfunds.core.domain.usecase.savings

/**
 * Use case to delete a specific savings goal.
 */
interface DeleteSavingsGoalUseCase {
    suspend operator fun invoke(goalId: String)
}