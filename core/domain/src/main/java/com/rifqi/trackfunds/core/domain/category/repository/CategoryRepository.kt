package com.rifqi.trackfunds.core.domain.category.repository

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Category repository.
 */
interface CategoryRepository {
    /** Fetches a filtered list of default categories AND categories owned by the current user. */
    fun getFilteredCategories(filter: CategoryFilter): Flow<List<Category>>

    /** Fetches a single category by its ID, can be a default or user-owned category. */
    suspend fun getCategoryById(categoryId: String): Result<Category>

    /** Fetches multiple categories by their IDs, can be a mix of default and user-owned. */
    suspend fun getCategoriesByIds(ids: List<String>): List<Category>

    /** Fetches a single DEFAULT category by its unique standard key. */
    suspend fun getCategoryByStandardKey(key: String): Category?

    /** Inserts a new category for the currently logged-in user. */
    suspend fun insertCategory(category: Category): Result<Unit>

    /** Updates a category, ONLY if it is owned by the user. */
    suspend fun updateCategory(category: Category): Result<Unit>

    /** Deletes a category by its ID, ONLY if it is owned by the user. */
    suspend fun deleteCategory(categoryId: String): Result<Unit>
}