package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category

/**
 * Gets a single category by its ID. Can be a default or user-owned category.
 */
interface GetCategoryUseCase {
    suspend operator fun invoke(categoryId: String): Result<Category>
}