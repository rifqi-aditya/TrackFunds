package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryByStandardKeyUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetCategoryByStandardKeyUseCase {
    override suspend operator fun invoke(key: String): Category? {
        return repository.getCategoryByStandardKey(key)
    }
}