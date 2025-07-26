package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import kotlinx.coroutines.flow.Flow

/**
 * Gets a continuous stream of filtered savings goals.
 */
interface GetFilteredSavingsGoalsUseCase {
    operator fun invoke(filter: SavingsFilter): Flow<List<SavingsGoalModel>>
}