package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.GoalDetails
import kotlinx.coroutines.flow.Flow

interface GetSavingsGoalDetailsUseCase {
    operator fun invoke(goalId: String): Flow<GoalDetails?>
}