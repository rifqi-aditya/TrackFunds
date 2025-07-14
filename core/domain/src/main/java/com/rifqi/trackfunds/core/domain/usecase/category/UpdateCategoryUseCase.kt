package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

interface UpdateCategoryUseCase {
    suspend operator fun invoke(category: CategoryItem)
}