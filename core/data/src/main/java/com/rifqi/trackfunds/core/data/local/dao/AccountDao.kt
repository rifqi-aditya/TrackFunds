package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rifqi.trackfunds.core.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    /**
     * Fetches all accounts for a specific user from the database, ordered by name.
     * @param userUid The ID of the currently logged-in user.
     * @return A Flow that emits a list of accounts, reacting to data changes.
     */
    @Query("SELECT * FROM accounts WHERE user_uid = :userUid ORDER BY name ASC")
    fun getAccounts(userUid: String): Flow<List<AccountEntity>> // Renamed for clarity

    /**
     * Fetches a specific account by its ID for a specific user.
     * @param accountId The ID of the account to fetch.
     * @param userUid The ID of the owner user.
     * @return The AccountEntity or null if not found.
     */
    @Query("SELECT * FROM accounts WHERE id = :accountId AND user_uid = :userUid")
    suspend fun getAccountById(accountId: String, userUid: String): AccountEntity? // Renamed for clarity

    /**
     * Inserts a single account. If it already exists, it will be replaced.
     * The AccountEntity object must have the userUid populated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    /**
     * Inserts a list of accounts (e.g., for pre-populating).
     * All entities in the list must have the userUid populated.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    /**
     * Updates an existing account in the database.
     * The update is based on the primary key of the entity.
     */
    @Update
    suspend fun updateAccount(account: AccountEntity)

    /**
     * Gets the total number of accounts for a specific user.
     * @param userUid The ID of the user whose accounts are to be counted.
     */
    // CHANGED: Added userUid to only count accounts for the current user.
    @Query("SELECT COUNT(id) FROM accounts WHERE user_uid = :userUid")
    suspend fun getAccountCount(userUid: String): Int

    /**

     * Deletes an account by its ID for a specific user, ensuring data integrity.
     * @param accountId The ID of the account to delete.
     * @param userUid The ID of the owner user.
     */
    // CHANGED: Added userUid to prevent deleting another user's account.
    @Query("DELETE FROM accounts WHERE id = :accountId AND user_uid = :userUid")
    suspend fun deleteAccountById(accountId: String, userUid: String)

    /**
     * Fetches a list of accounts by their IDs for a specific user.
     * @param ids A list of account IDs.
     * @param userUid The ID of the owner user.
     */
    // CHANGED: Added userUid to only fetch accounts for the current user.
    @Query("SELECT * FROM accounts WHERE id IN (:ids) AND user_uid = :userUid")
    suspend fun getAccountsByIds(ids: List<String>, userUid: String): List<AccountEntity>
}