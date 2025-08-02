package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal

/**
 * Creates a new savings goal.
 */
interface CreateSavingsGoalUseCase {
    suspend operator fun invoke(goal: SavingsGoal): Result<Unit>
}