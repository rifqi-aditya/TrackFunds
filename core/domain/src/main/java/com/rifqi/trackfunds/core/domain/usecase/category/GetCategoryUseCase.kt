package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

/**
 * Gets a single category by its ID. Can be a default or user-owned category.
 */
interface GetCategoryUseCase {
    suspend operator fun invoke(categoryId: String): Result<CategoryItem>
}