package com.rifqi.trackfunds.core.domain.savings.usecase

/**
 * Deletes a specific savings goal.
 */
interface DeleteSavingsGoalUseCase {
    suspend operator fun invoke(goalId: String): Result<Unit>
}