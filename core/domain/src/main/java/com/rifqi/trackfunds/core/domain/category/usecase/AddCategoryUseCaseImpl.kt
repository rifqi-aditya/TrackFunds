package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.common.util.generateStandardKeyFromName
import javax.inject.Inject

class AddCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : AddCategoryUseCase {
    override suspend operator fun invoke(category: Category): Result<Unit> {

        val newStandardKey = generateStandardKeyFromName(category.name)

        val categoryWithKey = category.copy(standardKey = newStandardKey)

        return repository.insertCategory(categoryWithKey)
    }
}