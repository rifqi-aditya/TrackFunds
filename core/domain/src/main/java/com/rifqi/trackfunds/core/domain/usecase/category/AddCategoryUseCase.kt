package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

interface AddCategoryUseCase {
    suspend operator fun invoke(category: CategoryItem)
}