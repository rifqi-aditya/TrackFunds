package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.Category
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetCategoryUseCase {
    override suspend operator fun invoke(categoryId: String): Result<Category> {
        // DIUBAH: Mengembalikan Result dari repository
        return repository.getCategoryById(categoryId)
    }
}