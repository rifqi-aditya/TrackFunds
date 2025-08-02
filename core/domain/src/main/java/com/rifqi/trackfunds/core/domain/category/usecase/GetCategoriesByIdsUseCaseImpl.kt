package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesByIdsUseCaseImpl @Inject constructor(private val repository: CategoryRepository) :
    GetCategoriesByIdsUseCase {
    override suspend operator fun invoke(ids: List<String>): List<Category> {
        return repository.getCategoriesByIds(ids)
    }
}