package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.AccountModel
import kotlinx.coroutines.flow.Flow

/**
 * Interface for the Account repository.
 * Defines the contract for data operations related to user accounts.
 */
interface AccountRepository {
    /** Fetches all accounts for the currently logged-in user. */
    fun getAllAccounts(): Flow<List<AccountModel>>

    /** Fetches a single account by its ID for the currently logged-in user. */
    suspend fun getAccountById(accountId: String): Result<AccountModel>

    /** Fetches a list of accounts by their IDs for the currently logged-in user. */
    suspend fun getAccountsByIds(ids: List<String>): List<AccountModel>

    /** Adds a new account for the currently logged-in user. */
    suspend fun addAccount(account: AccountModel): Result<Unit>

    /** Updates an existing account for the currently logged-in user. */
    suspend fun updateAccount(account: AccountModel): Result<Unit>

    /** Deletes an account by its ID for the currently logged-in user. */
    suspend fun deleteAccount(accountId: String): Result<Unit>
}