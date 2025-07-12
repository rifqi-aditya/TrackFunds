package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    override fun getAllAccounts(): Flow<List<AccountItem>> {
        return accountDao.getAllAccounts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(accountId: String): AccountItem? {
        return accountDao.getAccountById(accountId)?.toDomain()
    }

    override suspend fun getAccountsByIds(ids: List<String>): List<AccountItem> {
        return accountDao.getAccountsByIds(ids).map { it.toDomain() }
    }

    override suspend fun addAccount(account: AccountItem) {
        accountDao.insertAccount(account.toEntity())
    }

    override suspend fun updateAccount(account: AccountItem) {
        accountDao.updateAccount(account.toEntity())
    }

    override suspend fun deleteAccount(accountId: String) {
        accountDao.deleteAccountById(accountId)
    }
}