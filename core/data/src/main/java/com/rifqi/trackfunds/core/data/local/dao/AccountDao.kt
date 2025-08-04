package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.rifqi.trackfunds.core.data.local.entity.AccountEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for the accounts table.
 */
@Dao
interface AccountDao {

    /**
     * Observes list of accounts.
     *
     * @param userUid the user ID.
     * @return all accounts by user ID.
     */
    @Query("SELECT * FROM accounts WHERE user_uid = :userUid ORDER BY name ASC")
    fun getAccounts(userUid: String): Flow<List<AccountEntity>>

    /**
     * Select a account by id.
     *
     * @param accountId the account id.
     * @param userUid the user ID.
     * @return the account with accountId.
     */
    @Query("SELECT * FROM accounts WHERE id = :accountId AND user_uid = :userUid")
    suspend fun getAccountById(accountId: String, userUid: String): AccountEntity?

    /**
     * Select a list of accounts by ids.
     *
     * @param ids the list of account ids.
     * @param userUid the user ID.
     * @return the list of accounts with ids.
     */
    @Query("SELECT * FROM accounts WHERE id IN (:ids) AND user_uid = :userUid")
    suspend fun getAccountsByIds(ids: List<String>, userUid: String): List<AccountEntity>

    /**
     * Select count of accounts by user ID.
     *
     * @param userUid the user ID.
     * @return the count of accounts.
     */
    @Query("SELECT COUNT(id) FROM accounts WHERE user_uid = :userUid")
    suspend fun getAccountCount(userUid: String): Int

    /**
     * Insert or update an account in the database. If the account already exists, replace it.
     *
     * @param account the account to be inserted or updated.
     */
    @Upsert
    suspend fun upsert(account: AccountEntity)

    /**
     * Insert all accounts.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    /**
     * Delete a account by id.
     *
     * @param accountId the account id.
     * @param userUid the user ID.
     * @return the number of accounts deleted. This should always be 1.
     */
    @Query("DELETE FROM accounts WHERE id = :accountId AND user_uid = :userUid")
    suspend fun deleteAccountById(accountId: String, userUid: String)
}