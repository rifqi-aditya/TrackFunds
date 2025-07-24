package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

/**
 * Adds a new category for the current user.
 */
interface AddCategoryUseCase {
    suspend operator fun invoke(category: CategoryItem): Result<Unit>
}