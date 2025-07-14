package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import kotlinx.coroutines.flow.Flow

interface GetFilteredCategoriesUseCase {
    operator fun invoke(filter: CategoryFilter): Flow<List<CategoryItem>>
}