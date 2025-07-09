package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.model.filter.SavingsFilter
import kotlinx.coroutines.flow.Flow

interface GetFilteredSavingsGoalsUseCase {
    operator fun invoke(filter: SavingsFilter): Flow<List<SavingsGoalItem>>
}