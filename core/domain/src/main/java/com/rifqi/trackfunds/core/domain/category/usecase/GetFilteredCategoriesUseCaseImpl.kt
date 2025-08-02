package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilteredCategoriesUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetFilteredCategoriesUseCase {
    override operator fun invoke(filter: CategoryFilter): Flow<List<Category>> {
        return repository.getFilteredCategories(filter)
    }
}
