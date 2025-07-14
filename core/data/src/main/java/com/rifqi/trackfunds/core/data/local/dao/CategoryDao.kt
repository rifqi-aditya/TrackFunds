package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    /**
     * Mengambil daftar kategori berdasarkan filter yang diberikan.
     * Mendukung filter berdasarkan tipe transaksi dan status budget.
     */
    @Query(
        """
        SELECT * FROM categories
        WHERE
            (:type IS NULL OR type = :type)
            AND
            (
                :isUnbudgeted IS NULL OR
                (:isUnbudgeted = 1 AND id NOT IN (SELECT category_id FROM budgets WHERE period = :budgetPeriod)) OR
                (:isUnbudgeted = 0 AND id IN (SELECT category_id FROM budgets WHERE period = :budgetPeriod))
            )
        ORDER BY name ASC
    """
    )
    fun getFilteredCategories(
        type: TransactionType?,
        isUnbudgeted: Boolean?,
        budgetPeriod: String?
    ): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?

    @Query("SELECT * FROM categories WHERE id IN (:ids)")
    suspend fun getCategoriesByIds(ids: List<String>): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE standard_key = :key LIMIT 1")
    suspend fun getCategoryByStandardKey(key: String): CategoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Query("DELETE FROM categories WHERE id = :categoryId")
    suspend fun deleteCategoryById(categoryId: String)

    // @Query("DELETE FROM categories")
    // suspend fun deleteAll()
}