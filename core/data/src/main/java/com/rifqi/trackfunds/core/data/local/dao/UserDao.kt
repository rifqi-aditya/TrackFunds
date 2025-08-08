package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rifqi.trackfunds.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)

    /**
     * Mengambil profil pengguna sebagai Flow yang reaktif.
     * Namanya diubah agar konsisten dengan repository.
     */
    @Query("SELECT * FROM users WHERE uid = :uid LIMIT 1")
    fun getProfile(uid: String): Flow<UserEntity?> // <-- NAMA DISESUAIKAN

    /**
     * Mengambil pengguna untuk verifikasi login/register.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    /**
     * Menghapus pengguna dari database berdasarkan UID.
     * Fungsi ini dibutuhkan oleh deleteProfile di repository.
     */
    @Query("DELETE FROM users WHERE uid = :uid") // <-- FUNGSI BARU
    suspend fun deleteUserById(uid: String)

}