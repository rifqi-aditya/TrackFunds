package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.category.exception.CategoryNotFoundException
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val session: UserSessionRepository // ← ganti ke interface domain
) : CategoryRepository {

    override fun getFilteredCategories(filter: CategoryFilter): Flow<List<Category>> {
        return session.userUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                categoryDao.getFilteredCategories(
                    userUid = uid,
                    type = filter.type,
                    isUnbudgeted = filter.isUnbudgeted,
                    budgetPeriod = filter.budgetPeriod
                ).map { list -> list.map { it.toDomain() } }
            }
        }
        // .distinctUntilChanged() // opsional jika ingin menahan emisi duplikat
    }

    override suspend fun getCategoryById(categoryId: String): Result<Category> = try {
        val uid = session.requireActiveUserId()
        val entity = categoryDao.getCategoryById(categoryId, uid)
            ?: throw CategoryNotFoundException("Category with ID $categoryId not found.")
        Result.success(entity.toDomain())
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun getCategoriesByIds(ids: List<String>): List<Category> = try {
        val uid = session.requireActiveUserId()
        categoryDao.getCategoriesByIds(ids, uid).map { it.toDomain() }
    } catch (ce: CancellationException) {
        throw ce
    } catch (_: NotAuthenticatedException) {
        emptyList() // desain: belum login → list kosong
    }

    override suspend fun getCategoryByStandardKey(key: String): Category? = try {
        val uid = session.requireActiveUserId()
        // Jika DAO-mu sudah mendukung fallback default (userUid = NULL), cukup 1 query ini.
        // Kalau perlu fallback manual: coba cari user → jika null, coba default.
        categoryDao.getCategoryByStandardKey(key, uid)?.toDomain()
    } catch (ce: CancellationException) {
        throw ce
    } catch (_: NotAuthenticatedException) {
        null
    }

    override suspend fun insertCategory(category: Category): Result<Unit> = try {
        val uid = session.requireActiveUserId()
        categoryDao.insertCategory(category.toEntity(uid))
        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun updateCategory(category: Category): Result<Unit> {
        return try {
            val uid = session.requireActiveUserId()

            val existing = categoryDao.getCategoryById(category.id, uid)
                ?: throw CategoryNotFoundException("Category not found or not owned.")

            if (existing.userUid == null) {
                return Result.failure(IllegalStateException("Default categories cannot be modified."))
            }

            categoryDao.updateCategory(category.toEntity(uid))
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: NotAuthenticatedException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> {
        return try {
            val uid = session.requireActiveUserId()

            val entity = categoryDao.getCategoryById(categoryId, uid)
                ?: throw CategoryNotFoundException("Category not found or not owned.")

            if (entity.userUid == null) {
                return Result.failure(IllegalStateException("Default categories cannot be deleted."))
            }

            categoryDao.deleteCategoryById(categoryId, uid)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: NotAuthenticatedException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
