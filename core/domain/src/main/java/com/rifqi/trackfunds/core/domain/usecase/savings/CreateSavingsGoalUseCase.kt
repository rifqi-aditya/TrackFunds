package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal

/**
 * Creates a new savings goal.
 */
interface CreateSavingsGoalUseCase {
    suspend operator fun invoke(goal: SavingsGoal): Result<Unit>
}