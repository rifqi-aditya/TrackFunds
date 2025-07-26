package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesByIdsUseCaseImpl @Inject constructor(private val repository: CategoryRepository) :
    GetCategoriesByIdsUseCase {
    override suspend operator fun invoke(ids: List<String>): List<CategoryModel> {
        return repository.getCategoriesByIds(ids)
    }
}