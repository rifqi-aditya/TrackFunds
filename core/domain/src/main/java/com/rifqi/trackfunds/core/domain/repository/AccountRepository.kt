package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.AccountItem
import kotlinx.coroutines.flow.Flow

/**
 * Interface (kontrak) untuk Repository Akun.
 * Lapisan domain mendefinisikan 'apa' yang bisa dilakukan,
 * sementara lapisan data akan menyediakan implementasi 'bagaimana' cara melakukannya.
 */
interface AccountRepository {
    fun getAllAccounts(): Flow<List<AccountItem>>
    suspend fun getAccountById(accountId: String): AccountItem?
    suspend fun getAccountsByIds(ids: List<String>): List<AccountItem>
    suspend fun addAccount(account: AccountItem)
    suspend fun updateAccount(account: AccountItem)
    suspend fun deleteAccount(accountId: String)
}