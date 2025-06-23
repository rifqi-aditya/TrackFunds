package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import java.time.YearMonth
import javax.inject.Inject

class GetUnbudgetedCategoriesUseCaseImpl @Inject constructor(
    private val repository: CategoryRepository
) : GetUnbudgetedCategoriesUseCase {
    override operator fun invoke(period: YearMonth): Flow<List<CategoryItem>> {
        return repository.getUnbudgetedCategories(period)
    }
}