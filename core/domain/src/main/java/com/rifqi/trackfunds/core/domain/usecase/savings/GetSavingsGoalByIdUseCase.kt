package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import kotlinx.coroutines.flow.Flow

interface GetSavingsGoalByIdUseCase {
    suspend operator fun invoke(goalId: String): Flow<SavingsGoalItem?>
}