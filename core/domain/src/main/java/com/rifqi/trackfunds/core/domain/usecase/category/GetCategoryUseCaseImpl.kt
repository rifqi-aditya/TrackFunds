package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetCategoryUseCase {
    override suspend operator fun invoke(categoryId: String): Result<CategoryItem> {
        // DIUBAH: Mengembalikan Result dari repository
        return repository.getCategoryById(categoryId)
    }
}