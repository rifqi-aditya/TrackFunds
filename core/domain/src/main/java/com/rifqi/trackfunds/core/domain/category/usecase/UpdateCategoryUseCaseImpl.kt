package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.common.util.generateStandardKeyFromName
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import javax.inject.Inject

class UpdateCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : UpdateCategoryUseCase {
    override suspend operator fun invoke(category: Category): Result<Unit> {
        // Generate ulang standardKey dari nama yang mungkin baru
        val newStandardKey = generateStandardKeyFromName(category.name)

        val categoryWithKey = category.copy(standardKey = newStandardKey)

        return repository.updateCategory(categoryWithKey)
    }
}