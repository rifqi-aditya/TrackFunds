package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth

interface GetUnbudgetedCategoriesUseCase {
    operator fun invoke(period: YearMonth): Flow<List<CategoryItem>>
}