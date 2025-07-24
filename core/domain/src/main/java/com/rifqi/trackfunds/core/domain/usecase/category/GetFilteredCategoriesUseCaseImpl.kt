package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredCategoriesUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetFilteredCategoriesUseCase {
    override operator fun invoke(filter: CategoryFilter): Flow<List<CategoryItem>> {
        return repository.getFilteredCategories(filter)
    }
}
