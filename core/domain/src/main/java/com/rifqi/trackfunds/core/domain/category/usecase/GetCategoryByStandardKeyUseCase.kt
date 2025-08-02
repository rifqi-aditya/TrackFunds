package com.rifqi.trackfunds.core.domain.category.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category

/**
 * Gets a single default category by its standard key.
 */
interface GetCategoryByStandardKeyUseCase {
    suspend operator fun invoke(key: String): Category?
}