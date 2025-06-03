package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton // Hilt: Menandakan instance ini akan menjadi Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getCategories(): Flow<List<CategoryItem>> {
        return categoryDao.getAllCategories().map { entityList ->
            entityList.map { it.toDomain() } // Map dari List<CategoryEntity> ke List<CategoryItem>
        }

    }

    // Implementasi untuk getCategoriesByType jika Anda menambahkannya di interface
//    override fun getCategoriesByType(type: TransactionType): Flow<List<CategoryItem>> {
//         return categoryDao.getCategoriesByType(type).map { entityList ->
//             entityList.map { it.toDomain() }
//         }
//    }
}