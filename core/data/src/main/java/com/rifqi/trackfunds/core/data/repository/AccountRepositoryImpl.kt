package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAllAccounts(userUid: String): Flow<List<Account>> {
        return accountDao.getAccounts(userUid).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(accountId: String, userUid: String): Result<Account> {
        return runCatching {
            accountDao.getAccountById(accountId, userUid)?.toDomain()
                ?: throw Exception("Account not found.")
        }
    }

    override suspend fun getAccountsByIds(ids: List<String>, userUid: String): List<Account> {
        return accountDao.getAccountsByIds(ids, userUid).map { it.toDomain() }
    }

    override suspend fun saveAccount(account: Account, userUid: String): Result<Unit> {
        return runCatching {
            val accountEntity = account.toEntity(userUid)
            accountDao.upsert(accountEntity)
        }
    }

    override suspend fun deleteAccount(accountId: String, userUid: String): Result<Unit> {
        return runCatching {
            accountDao.deleteAccountById(accountId, userUid)
        }
    }
}