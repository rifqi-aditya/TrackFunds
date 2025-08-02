package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetCategoryUseCase {
    override suspend operator fun invoke(categoryId: String): Result<Category> {
        // DIUBAH: Mengembalikan Result dari repository
        return repository.getCategoryById(categoryId)
    }
}