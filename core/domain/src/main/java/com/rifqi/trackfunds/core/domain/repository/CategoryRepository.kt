package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getFilteredCategories(filter: CategoryFilter): Flow<List<CategoryItem>>

    suspend fun getCategoryById(categoryId: String): CategoryItem?

    suspend fun getCategoriesByIds(ids: List<String>): List<CategoryItem>

    suspend fun getCategoryByStandardKey(key: String): CategoryItem?

    suspend fun insertCategory(category: CategoryItem)

    suspend fun updateCategory(category: CategoryItem)

    suspend fun deleteCategory(categoryId: String)
}