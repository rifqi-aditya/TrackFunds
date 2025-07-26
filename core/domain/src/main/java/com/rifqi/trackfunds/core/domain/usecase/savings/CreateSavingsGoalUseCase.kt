package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel

/**
 * Creates a new savings goal.
 */
interface CreateSavingsGoalUseCase {
    // DIUBAH: Mengembalikan Result<Unit>
    suspend operator fun invoke(goal: SavingsGoalModel): Result<Unit>
}