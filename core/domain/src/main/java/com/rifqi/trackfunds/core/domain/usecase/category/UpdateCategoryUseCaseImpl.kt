package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.utils.generateStandardKeyFromName
import javax.inject.Inject

class UpdateCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : UpdateCategoryUseCase {
    override suspend operator fun invoke(category: CategoryItem): Result<Unit> {
        // Generate ulang standardKey dari nama yang mungkin baru
        val newStandardKey = generateStandardKeyFromName(category.name)

        val categoryWithKey = category.copy(standardKey = newStandardKey)

        return repository.updateCategory(categoryWithKey)
    }
}