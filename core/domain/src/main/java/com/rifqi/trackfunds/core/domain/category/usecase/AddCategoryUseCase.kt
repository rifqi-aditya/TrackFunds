package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category

/**
 * Adds a new category for the current user.
 */
interface AddCategoryUseCase {
    suspend operator fun invoke(category: Category): Result<Unit>
}