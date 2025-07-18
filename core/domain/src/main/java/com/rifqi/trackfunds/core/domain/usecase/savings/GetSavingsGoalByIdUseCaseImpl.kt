package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavingsGoalByIdUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : GetSavingsGoalByIdUseCase {
    override suspend operator fun invoke(goalId: String): Flow<SavingsGoalItem?> {
        return repository.getGoalById(goalId)
    }
}