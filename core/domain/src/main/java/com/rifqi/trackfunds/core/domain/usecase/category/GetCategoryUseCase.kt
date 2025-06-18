package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

interface GetCategoryUseCase {
    suspend operator fun invoke(categoryId: String): CategoryItem?
}