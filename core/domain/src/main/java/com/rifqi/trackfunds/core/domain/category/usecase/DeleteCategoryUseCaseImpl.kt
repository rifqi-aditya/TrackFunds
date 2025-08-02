package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import javax.inject.Inject

class DeleteCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : DeleteCategoryUseCase {
    override suspend operator fun invoke(categoryId: String): Result<Unit> {
        // DIUBAH: Mengembalikan Result dari repository
        return repository.deleteCategory(categoryId)
    }
}