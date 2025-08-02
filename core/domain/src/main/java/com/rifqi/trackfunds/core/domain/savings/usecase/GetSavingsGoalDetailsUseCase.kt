package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.model.GoalDetails
import kotlinx.coroutines.flow.Flow

interface GetSavingsGoalDetailsUseCase {
    operator fun invoke(goalId: String): Flow<GoalDetails?>
}