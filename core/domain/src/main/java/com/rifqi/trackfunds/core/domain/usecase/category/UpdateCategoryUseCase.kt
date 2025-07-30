package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.Category

/**
 * Updates a user-owned category.
 */
interface UpdateCategoryUseCase {
    suspend operator fun invoke(category: Category): Result<Unit>
}