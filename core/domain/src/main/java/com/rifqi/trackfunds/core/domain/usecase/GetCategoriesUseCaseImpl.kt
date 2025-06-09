package com.rifqi.trackfunds.core.domain.usecase

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCaseImpl @Inject constructor(
    private val categoryRepository: CategoryRepository
) : GetCategoriesUseCase {

    override operator fun invoke(): Flow<List<CategoryItem>> {
        return categoryRepository.getCategories()
    }
}