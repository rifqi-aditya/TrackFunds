package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import kotlinx.coroutines.flow.Flow

/**
 * Gets a continuous stream of filtered categories.
 */
interface GetFilteredCategoriesUseCase {
    operator fun invoke(filter: CategoryFilter): Flow<List<Category>>
}