package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : DeleteCategoryUseCase {
    override suspend operator fun invoke(categoryId: String) {
        repository.deleteCategory(categoryId)
    }
}