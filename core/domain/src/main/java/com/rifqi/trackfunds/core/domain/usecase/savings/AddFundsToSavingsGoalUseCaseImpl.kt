package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import java.math.BigDecimal
import javax.inject.Inject

class AddFundsToSavingsGoalUseCaseImpl @Inject constructor(
    private val repository: SavingsRepository
) : AddFundsToSavingsGoalUseCase {
    override suspend operator fun invoke(goalId: String, amount: BigDecimal): Result<Unit> {
        return repository.addFundsToGoal(goalId, amount)
    }
}