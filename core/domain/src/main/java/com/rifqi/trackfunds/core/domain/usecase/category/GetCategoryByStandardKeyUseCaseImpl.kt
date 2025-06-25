package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoryByStandardKeyUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetCategoryByStandardKeyUseCase {

    override suspend operator fun invoke(key: String): CategoryItem? {
        return repository.findCategoryByStandardKey(key)
    }
}