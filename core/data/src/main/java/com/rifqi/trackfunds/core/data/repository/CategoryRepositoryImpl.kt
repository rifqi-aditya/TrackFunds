package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth
import java.time.format.DateTimeFormatter
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

    override suspend fun getCategoryById(categoryId: String): CategoryItem? {
        return categoryDao.getCategoryById(categoryId)?.toDomain()
    }

    override suspend fun getCategoriesByIds(ids: List<String>): List<CategoryItem> {
        return categoryDao.getCategoriesByIds(ids).map { it.toDomain() }
    }

    override suspend fun findCategoryByStandardKey(key: String): CategoryItem? {
        return categoryDao.findByCategoryKey(key)?.toDomain()
    }

    override fun getUnbudgetedCategories(period: YearMonth): Flow<List<CategoryItem>> {
        val periodString = period.format(DateTimeFormatter.ofPattern("yyyy-MM"))
        return categoryDao.getUnbudgetedCategories(periodString).map { entities ->
            entities.map { it.toDomain() }
        }
    }

}