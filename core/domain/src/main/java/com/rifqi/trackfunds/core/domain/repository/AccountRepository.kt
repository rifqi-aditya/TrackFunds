package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.AccountItem
import kotlinx.coroutines.flow.Flow

/**
 * Interface (kontrak) untuk Repository Akun.
 * Lapisan domain mendefinisikan 'apa' yang bisa dilakukan,
 * sementara lapisan data akan menyediakan implementasi 'bagaimana' cara melakukannya.
 */
interface AccountRepository {

    /**
     * Mengambil semua akun sebagai stream data yang reaktif.
     */
    fun getAccounts(): Flow<List<AccountItem>>

    // Di masa depan, Anda bisa menambahkan fungsi lain di sini, seperti:
    // suspend fun getAccountById(id: String): AccountItem?
    // suspend fun addAccount(account: AccountItem)
    // suspend fun updateAccount(account: AccountItem)
    // suspend fun deleteAccount(id: String)
}