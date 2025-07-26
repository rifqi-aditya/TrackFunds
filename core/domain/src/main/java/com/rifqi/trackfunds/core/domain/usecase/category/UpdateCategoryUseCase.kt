package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryModel

/**
 * Updates a user-owned category.
 */
interface UpdateCategoryUseCase {
    suspend operator fun invoke(category: CategoryModel): Result<Unit>
}