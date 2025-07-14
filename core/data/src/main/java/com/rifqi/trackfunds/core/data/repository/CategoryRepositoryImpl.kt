package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getFilteredCategories(filter: CategoryFilter): Flow<List<CategoryItem>> {
        return categoryDao.getFilteredCategories(
            filter.type,
            filter.isUnbudgeted,
            filter.budgetPeriod
        ).map { entityList ->
            entityList.map { it.toDomain() }
        }
    }

    override suspend fun getCategoryById(categoryId: String): CategoryItem? {
        return categoryDao.getCategoryById(categoryId)?.toDomain()
    }

    override suspend fun getCategoriesByIds(ids: List<String>): List<CategoryItem> {
        return categoryDao.getCategoriesByIds(ids).map { it.toDomain() }
    }

    override suspend fun getCategoryByStandardKey(key: String): CategoryItem? {
        return categoryDao.getCategoryByStandardKey(key)?.toDomain()
    }

    override suspend fun insertCategory(category: CategoryItem) {
        categoryDao.insertCategory(category.toEntity())
    }

    override suspend fun updateCategory(category: CategoryItem) {
        categoryDao.updateCategory(category.toEntity())
    }

    override suspend fun deleteCategory(categoryId: String) {
        categoryDao.deleteCategoryById(categoryId)
    }
}