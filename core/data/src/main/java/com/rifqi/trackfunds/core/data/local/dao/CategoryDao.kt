package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: String): CategoryEntity?

    @Query("SELECT * FROM categories WHERE type = :transactionType")
    fun getCategoriesByType(transactionType: TransactionType): Flow<List<CategoryEntity>>

    // Untuk pre-populate
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCategories(categories: List<CategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Query("""
        SELECT * FROM categories
        WHERE type = 'EXPENSE' AND id NOT IN (
            SELECT category_id FROM budgets WHERE period = :period
        )
    """)
    fun getUnbudgetedCategories(period: String): Flow<List<CategoryEntity>>

    // Fungsi lain seperti getById, update, delete bisa ditambahkan nanti
    // @Query("DELETE FROM categories")
    // suspend fun deleteAll()
}