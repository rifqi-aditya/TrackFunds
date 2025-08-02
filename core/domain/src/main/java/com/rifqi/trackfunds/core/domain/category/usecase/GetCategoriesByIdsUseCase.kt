package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category

/**
 * Gets multiple categories by their IDs.
 */
interface GetCategoriesByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): List<Category>
}