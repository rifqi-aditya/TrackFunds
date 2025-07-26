package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import kotlinx.coroutines.flow.Flow

/**
 * Gets a continuous stream of a single savings goal by its ID.
 */
interface GetSavingsGoalByIdUseCase {
    // DIUBAH: Menghapus 'suspend' agar konsisten mengembalikan Flow
    operator fun invoke(goalId: String): Flow<SavingsGoalModel?>
}