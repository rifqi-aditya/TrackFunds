package com.rifqi.trackfunds.core.domain.account.repository

import com.rifqi.trackfunds.core.domain.account.model.Account
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Account repository.
 * Defines the contract for data operations related to user accounts.
 */
interface AccountRepository {
    /** Fetches all accounts for the currently logged-in user. */
    fun getAllAccounts(): Flow<List<Account>>

    /** Fetches a single account by its ID for the currently logged-in user. */
    suspend fun getAccountById(accountId: String): Result<Account>

    /** Fetches a list of accounts by their IDs for the currently logged-in user. */
    suspend fun getAccountsByIds(ids: List<String>): List<Account>

    /**
     * Saves a new account or updates an existing one for the currently logged-in user.
     * @param account The account object to be saved.
     */
    suspend fun saveAccount(account: Account): Result<Unit>

    /** Deletes an account by its ID for the currently logged-in user. */
    suspend fun deleteAccount(accountId: String): Result<Unit>
}

