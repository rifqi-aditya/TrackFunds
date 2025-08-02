package com.rifqi.trackfunds.core.domain.savings.usecase

import com.rifqi.trackfunds.core.domain.savings.model.SavingsGoal
import com.rifqi.trackfunds.core.domain.savings.model.SavingsFilter
import kotlinx.coroutines.flow.Flow

/**
 * Gets a continuous stream of filtered savings goals.
 */
interface GetFilteredSavingsGoalsUseCase {
    operator fun invoke(filter: SavingsFilter): Flow<List<SavingsGoal>>
}