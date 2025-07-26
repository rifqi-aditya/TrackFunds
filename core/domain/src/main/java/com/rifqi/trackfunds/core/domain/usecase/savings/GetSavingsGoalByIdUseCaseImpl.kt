package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavingsGoalByIdUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : GetSavingsGoalByIdUseCase {
    override operator fun invoke(goalId: String): Flow<SavingsGoalModel?> {
        return repository.getGoalById(goalId)
    }
}