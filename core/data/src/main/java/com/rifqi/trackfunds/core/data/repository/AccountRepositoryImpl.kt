package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.account.exception.AccountNotFoundException
import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.common.exception.AuthRequiredException
import com.rifqi.trackfunds.core.domain.common.repository.UserSessionProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val sessionProvider: UserSessionProvider
) : AccountRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllAccounts(): Flow<List<Account>> {
        return sessionProvider.getUidFlow().flatMapLatest { userUid ->
            if (userUid == null) flowOf(emptyList())
            else accountDao.getAccounts(userUid).map { entities -> entities.map { it.toDomain() } }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAccountCount(): Flow<Int> {
        return sessionProvider.getUidFlow().flatMapLatest { userUid ->
            if (userUid == null) flowOf(0)
            else accountDao.observeAccountCount(userUid)
        }
    }

    override suspend fun getAccountById(accountId: String): Result<Account> {
        return runCatching {
            val userUid = sessionProvider.getUid()

            accountDao.getAccountById(accountId, userUid)?.toDomain()
                ?: throw AccountNotFoundException("Account with ID $accountId not found.")
        }
    }

    override suspend fun getAccountsByIds(ids: List<String>): List<Account> {
        return try {
            // 1. Ambil userUid secara internal
            val userUid = sessionProvider.getUid()

            // 2. Lanjutkan logika
            accountDao.getAccountsByIds(ids, userUid).map { it.toDomain() }
        } catch (e: AuthRequiredException) {
            // 3. Jika user tidak login, kembalikan list kosong dengan aman
            emptyList()
        }
    }

    override suspend fun saveAccount(account: Account): Result<Unit> {
        return runCatching {
            val userUid = sessionProvider.getUid()
            val accountEntity = account.toEntity(userUid)
            accountDao.upsert(accountEntity)
        }
    }

    override suspend fun deleteAccount(accountId: String): Result<Unit> {
        return runCatching {
            val userUid = sessionProvider.getUid()
            accountDao.deleteAccountById(accountId, userUid)
        }
    }
}