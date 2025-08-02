package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

/**
 * Gets a continuous stream of a single savings goal by its ID.
 */
interface GetSavingsGoalByIdUseCase {
    operator fun invoke(goalId: String): Flow<SavingsGoal?>
}