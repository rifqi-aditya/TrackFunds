package com.rifqi.trackfunds.core.domain.usecase.savings

/**
 * Deletes a specific savings goal.
 */
interface DeleteSavingsGoalUseCase {
    // DIUBAH: Mengembalikan Result<Unit>
    suspend operator fun invoke(goalId: String): Result<Unit>
}