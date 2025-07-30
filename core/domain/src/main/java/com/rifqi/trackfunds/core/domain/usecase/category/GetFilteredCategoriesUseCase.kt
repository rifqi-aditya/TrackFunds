package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import kotlinx.coroutines.flow.Flow

/**
 * Gets a continuous stream of filtered categories.
 */
interface GetFilteredCategoriesUseCase {
    operator fun invoke(filter: CategoryFilter): Flow<List<Category>>
}