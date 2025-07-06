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
     * Mengambil semua akun dari database, diurutkan berdasarkan nama.
     * Mengembalikan Flow agar UI bisa bereaksi terhadap perubahan data.
     */
    @Query("SELECT * FROM accounts ORDER BY name ASC")
    fun getAccounts(): Flow<List<AccountEntity>>

    /**
     * Mengambil satu akun spesifik berdasarkan ID-nya.
     * Dibuat 'suspend' karena merupakan operasi database satu kali.
     * Dibutuhkan untuk mengambil detail akun sebelum saldonya diupdate.
     * @return AccountEntity atau null jika tidak ditemukan.
     */
    @Query("SELECT * FROM accounts WHERE id = :accountId")
    suspend fun getAccountById(accountId: String): AccountEntity?

    /**
     * Memasukkan satu akun. Jika sudah ada, akan diganti.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity)

    /**
     * Memasukkan daftar akun (untuk pre-populate).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accounts: List<AccountEntity>)

    /**
     * Memperbarui data akun yang sudah ada di database.
     * Penting untuk menyimpan perubahan saldo.
     */
    @Update
    suspend fun updateAccount(account: AccountEntity)

    /**
     * Mengambil jumlah total akun di database.
     * Berguna untuk logika pre-populate di AppDatabase.
     */
    @Query("SELECT COUNT(id) FROM accounts")
    suspend fun getAccountCount(): Int

    /**
     * (Opsional untuk masa depan) Menghapus akun berdasarkan ID.
     */
    @Query("DELETE FROM accounts WHERE id = :accountId")
    suspend fun deleteAccountById(accountId: String)

    @Query("SELECT * FROM accounts WHERE id IN (:ids)")
    suspend fun getAccountsByIds(ids: List<String>): List<AccountEntity>
}