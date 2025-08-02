package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the categories table.
 */
@Dao
interface CategoryDao {

    /**
     * Fetches a filtered list of categories.
     * It returns BOTH default categories (userUid is NULL) AND categories
     * owned by the specific user.
     */
    @Query(
        """
        SELECT * FROM categories
        WHERE
            (:type IS NULL OR type = :type)
            AND
            (
                :isUnbudgeted IS NULL OR
                (:isUnbudgeted = 1 AND id NOT IN (SELECT category_id FROM budgets WHERE period = :budgetPeriod AND user_uid = :userUid)) OR
                (:isUnbudgeted = 0 AND id IN (SELECT category_id FROM budgets WHERE period = :budgetPeriod AND user_uid = :userUid))
            )
            AND (user_uid = :userUid OR user_uid IS NULL)
        ORDER BY name ASC
    """
    )
    // DIUBAH: Menambahkan userUid dan logika WHERE (user_uid = :userUid OR user_uid IS NULL)
    fun getFilteredCategories(
        userUid: String,
        type: TransactionType?,
        isUnbudgeted: Boolean?,
        budgetPeriod: String?
    ): Flow<List<CategoryEntity>>

    /**
     * Fetches a single category by its ID.
     * Can fetch either a default category or a user-owned category.
     */
    // DIUBAH: Menambahkan userUid dan logika WHERE
    @Query("SELECT * FROM categories WHERE id = :categoryId AND (user_uid = :userUid OR user_uid IS NULL)")
    suspend fun getCategoryById(categoryId: String, userUid: String): CategoryEntity?

    /**
     * Fetches multiple categories by their IDs.
     * Can fetch a mix of default and user-owned categories.
     */
    // DIUBAH: Menambahkan userUid dan logika WHERE
    @Query("SELECT * FROM categories WHERE id IN (:ids) AND (user_uid = :userUid OR user_uid IS NULL)")
    suspend fun getCategoriesByIds(ids: List<String>, userUid: String): List<CategoryEntity>

    /**
     * Fetches a default category by its standard key.
     * This query explicitly only looks for default categories.
     */
    // DITAMBAHKAN: Pengecekan IS NULL untuk memastikan hanya kategori default yang diambil
    @Query("SELECT * FROM categories WHERE standard_key = :key AND user_uid IS NULL LIMIT 1")
    suspend fun getCategoryByStandardKey(key: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    /**
     * Deletes a category by its ID, ONLY if it is owned by the user.
     * This query will NOT delete default categories.
     */
    // DIUBAHKAN: Query DELETE sekarang sangat spesifik untuk user
    @Query("DELETE FROM categories WHERE id = :categoryId AND user_uid = :userUid")
    suspend fun deleteCategoryById(categoryId: String, userUid: String)
}