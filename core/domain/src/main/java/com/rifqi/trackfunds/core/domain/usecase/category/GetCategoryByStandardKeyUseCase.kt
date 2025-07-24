package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

/**
 * Gets a single default category by its standard key.
 */
interface GetCategoryByStandardKeyUseCase {
    suspend operator fun invoke(key: String): CategoryItem?
}