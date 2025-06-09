package com.rifqi.trackfunds.core.domain.usecase

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import kotlinx.coroutines.flow.Flow

interface GetCategoriesUseCase {
    operator fun invoke(): Flow<List<CategoryItem>>
}