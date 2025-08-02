package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category

/**
 * Updates a user-owned category.
 */
interface UpdateCategoryUseCase {
    suspend operator fun invoke(category: Category): Result<Unit>
}