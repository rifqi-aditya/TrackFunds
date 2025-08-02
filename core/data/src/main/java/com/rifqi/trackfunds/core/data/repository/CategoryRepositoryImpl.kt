package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : CategoryRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFilteredCategories(filter: CategoryFilter): Flow<List<Category>> {
        return userPreferencesRepository.userUid.flatMapLatest { userUid ->
            if (userUid == null) return@flatMapLatest flowOf(emptyList())

            categoryDao.getFilteredCategories(
                userUid = userUid,
                type = filter.type,
                isUnbudgeted = filter.isUnbudgeted,
                budgetPeriod = filter.budgetPeriod
            ).map { entityList ->
                entityList.map { it.toDomain() }
            }
        }
    }

    override suspend fun getCategoryById(categoryId: String): Result<Category> {
        return try {
            val userUid = userPreferencesRepository.userUid.first()
                ?: return Result.failure(Exception("User not logged in."))

            val category = categoryDao.getCategoryById(categoryId, userUid)?.toDomain()
                ?: return Result.failure(Exception("Category not found."))

            Result.success(category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCategoriesByIds(ids: List<String>): List<Category> {
        val userUid = userPreferencesRepository.userUid.first() ?: return emptyList()
        return categoryDao.getCategoriesByIds(ids, userUid).map { it.toDomain() }
    }

    override suspend fun getCategoryByStandardKey(key: String): Category? {
        // Fungsi ini tidak butuh userUid karena hanya mencari kategori default
        return categoryDao.getCategoryByStandardKey(key)?.toDomain()
    }

    override suspend fun insertCategory(category: Category): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUid.first()
                ?: return Result.failure(Exception("User not logged in."))
            categoryDao.insertCategory(category.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateCategory(category: Category): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUid.first()
                ?: return Result.failure(Exception("User not logged in."))

            // PENTING: Lakukan pengecekan di sini untuk melindungi kategori default
            val existingCategory = categoryDao.getCategoryById(category.id, userUid)
                ?: return Result.failure(Exception("Category not found."))

            if (existingCategory.userUid == null) {
                return Result.failure(Exception("Default categories cannot be modified."))
            }

            categoryDao.updateCategory(category.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUid.first()
                ?: return Result.failure(Exception("User not logged in."))

            val categoryToDelete = categoryDao.getCategoryById(categoryId, userUid)
                ?: return Result.failure(Exception("Category not found or you don't have permission to delete it."))

            if (categoryToDelete.userUid == null) {
                return Result.failure(Exception("Default categories cannot be deleted."))
            }

            categoryDao.deleteCategoryById(categoryId, userUid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}