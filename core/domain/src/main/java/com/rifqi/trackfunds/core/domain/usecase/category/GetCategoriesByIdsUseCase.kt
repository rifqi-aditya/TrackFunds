package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

/**
 * Gets multiple categories by their IDs.
 */
interface GetCategoriesByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): List<CategoryItem>
}