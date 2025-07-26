package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.utils.generateStandardKeyFromName
import javax.inject.Inject

class AddCategoryUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : AddCategoryUseCase {
    override suspend operator fun invoke(category: CategoryModel): Result<Unit> {

        val newStandardKey = generateStandardKeyFromName(category.name)

        val categoryWithKey = category.copy(standardKey = newStandardKey)

        return repository.insertCategory(categoryWithKey)
    }
}