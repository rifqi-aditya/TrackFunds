package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import kotlinx.coroutines.flow.Flow

/**
 * Gets a continuous stream of active savings goals.
 */
interface GetActiveSavingsGoalsUseCase {
    operator fun invoke(): Flow<List<SavingsGoal>>
}