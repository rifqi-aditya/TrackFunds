package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryModel

/**
 * Gets multiple categories by their IDs.
 */
interface GetCategoriesByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): List<CategoryModel>
}