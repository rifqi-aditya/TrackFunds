package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

interface GetSavingsGoalByIdUseCase {
    suspend operator fun invoke(goalId: String): Flow<SavingsGoal?>
}