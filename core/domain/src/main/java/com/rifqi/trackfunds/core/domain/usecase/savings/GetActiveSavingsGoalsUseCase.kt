package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import kotlinx.coroutines.flow.Flow

interface GetActiveSavingsGoalsUseCase {
    operator fun invoke(): Flow<List<SavingsGoalItem>>
}