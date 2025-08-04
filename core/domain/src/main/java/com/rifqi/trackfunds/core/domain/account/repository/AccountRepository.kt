package com.rifqi.trackfunds.core.domain.account.repository

import com.rifqi.trackfunds.core.domain.account.model.Account
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Account repository.
 * Defines the contract for data operations related to user accounts.
 */
interface AccountRepository {
    /** Fetches all accounts for the currently logged-in user. */
    fun getAllAccounts(userUid: String): Flow<List<Account>>

    /** Fetches a single account by its ID for the currently logged-in user. */
    suspend fun getAccountById(accountId: String, userUid: String): Result<Account>

    /** Fetches a list of accounts by their IDs for the currently logged-in user. */
    suspend fun getAccountsByIds(ids: List<String>, userUid: String): List<Account>

    /**
     * Saves a new account or updates an existing one for the currently logged-in user.
     * @param account The account object to be saved.
     * @param userUid The UID of the currently logged-in user.
     */
    suspend fun saveAccount(account: Account, userUid: String): Result<Unit>

    /** Deletes an account by its ID for the currently logged-in user. */
    suspend fun deleteAccount(accountId: String, userUid: String): Result<Unit>
}