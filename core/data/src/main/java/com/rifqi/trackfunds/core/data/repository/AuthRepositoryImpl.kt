package com.rifqi.trackfunds.core.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userPreferencesRepository: UserPreferencesRepository
) : AuthRepository {

    /**
     * Mencoba untuk login dengan email dan password.
     * Jika berhasil, simpan UID ke DataStore.
     * Mengembalikan Result.success atau Result.failure dengan exception.
     */
    override suspend fun login(email: String, pass: String): Result<Unit> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, pass).await()
            val uid = result.user?.uid
            if (uid != null) {
                userPreferencesRepository.saveUserUid(uid)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to get user ID."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mencoba untuk mendaftarkan pengguna baru dengan email dan password.
     * Jika berhasil, simpan UID ke DataStore.
     * Mengembalikan Result.success atau Result.failure dengan exception.
     */
    override suspend fun register(email: String, pass: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, pass).await()
            val uid = result.user?.uid
            if (uid != null) {
                userPreferencesRepository.saveUserUid(uid) // Tetap simpan sesi
                Result.success(uid) // Kembalikan UID jika berhasil
            } else {
                Result.failure(Exception("Failed to get user ID after registration."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Melakukan logout dari Firebase dan menghapus sesi UID dari DataStore.
     */
    override suspend fun logout() {
        auth.signOut()
        userPreferencesRepository.clearUserUid()
    }
}