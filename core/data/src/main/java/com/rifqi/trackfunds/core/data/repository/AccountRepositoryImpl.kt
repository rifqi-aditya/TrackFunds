package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
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

    override fun getAccounts(): Flow<List<AccountItem>> {
        // Ambil data dari DAO, lalu map setiap List<AccountEntity> menjadi List<AccountItem>
        return accountDao.getAccounts().map { entityList ->
            entityList.map { it.toDomain() }
        }
    }

    override suspend fun getAccountById(accountId: String): AccountItem? {
        return accountDao.getAccountById(accountId)?.toDomain()
    }

    override suspend fun getAccountsByIds(ids: List<String>): List<AccountItem> {
        return accountDao.getAccountsByIds(ids).map { it.toDomain() }
    }
}