package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : UpdateCategoryUseCase {
    override suspend operator fun invoke(category: CategoryItem) {
        repository.updateCategory(category)
    }
}