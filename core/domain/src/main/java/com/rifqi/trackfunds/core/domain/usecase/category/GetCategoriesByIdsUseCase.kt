package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.Category

/**
 * Gets multiple categories by their IDs.
 */
interface GetCategoriesByIdsUseCase {
    suspend operator fun invoke(ids: List<String>): List<Category>
}