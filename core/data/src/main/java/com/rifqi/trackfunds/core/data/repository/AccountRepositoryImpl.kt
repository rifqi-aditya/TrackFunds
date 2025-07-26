package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : AccountRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllAccounts(): Flow<List<AccountModel>> {
        return userPreferencesRepository.userUidFlow.flatMapLatest { uid ->
            if (uid == null) {
                flowOf(emptyList())
            } else {
                accountDao.getAccounts(uid).map { entities ->
                    entities.map { it.toDomain() }
                }
            }
        }
    }

    override suspend fun getAccountById(accountId: String): Result<AccountModel> {
        return try {
            val uid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            val accountEntity = accountDao.getAccountById(accountId, uid)

            if (accountEntity != null) {
                Result.success(accountEntity.toDomain())
            } else {
                Result.failure(Exception("Account with ID $accountId not found."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAccountsByIds(ids: List<String>): List<AccountModel> {
        val uid = userPreferencesRepository.userUidFlow.first() ?: return emptyList()
        return accountDao.getAccountsByIds(ids, uid).map { it.toDomain() }
    }

    override suspend fun addAccount(account: AccountModel): Result<Unit> {
        return try {
            val uid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            accountDao.insertAccount(account.toEntity(uid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAccount(account: AccountModel): Result<Unit> {
        return try {
            val uid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            accountDao.updateAccount(account.toEntity(uid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(accountId: String): Result<Unit> {
        return try {
            val uid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            accountDao.deleteAccountById(accountId, uid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}