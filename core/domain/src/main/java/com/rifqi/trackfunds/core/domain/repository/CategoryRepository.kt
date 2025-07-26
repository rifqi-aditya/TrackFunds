package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Category repository.
 */
interface CategoryRepository {
    /** Fetches a filtered list of default categories AND categories owned by the current user. */
    fun getFilteredCategories(filter: CategoryFilter): Flow<List<CategoryModel>>

    /** Fetches a single category by its ID, can be a default or user-owned category. */
    suspend fun getCategoryById(categoryId: String): Result<CategoryModel>

    /** Fetches multiple categories by their IDs, can be a mix of default and user-owned. */
    suspend fun getCategoriesByIds(ids: List<String>): List<CategoryModel>

    /** Fetches a single DEFAULT category by its unique standard key. */
    suspend fun getCategoryByStandardKey(key: String): CategoryModel?

    /** Inserts a new category for the currently logged-in user. */
    suspend fun insertCategory(category: CategoryModel): Result<Unit>

    /** Updates a category, ONLY if it is owned by the user. */
    suspend fun updateCategory(category: CategoryModel): Result<Unit>

    /** Deletes a category by its ID, ONLY if it is owned by the user. */
    suspend fun deleteCategory(categoryId: String): Result<Unit>
}