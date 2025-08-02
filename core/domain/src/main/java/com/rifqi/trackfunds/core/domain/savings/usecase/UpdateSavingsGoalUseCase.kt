package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal

interface UpdateSavingsGoalUseCase {
    suspend operator fun invoke(goal: SavingsGoal): Result<Unit>
}