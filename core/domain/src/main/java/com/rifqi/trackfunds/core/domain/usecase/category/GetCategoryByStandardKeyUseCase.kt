package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.Category

/**
 * Gets a single default category by its standard key.
 */
interface GetCategoryByStandardKeyUseCase {
    suspend operator fun invoke(key: String): Category?
}