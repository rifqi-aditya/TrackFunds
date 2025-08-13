package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.rifqi.trackfunds.core.data.local.dao.UserDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.domain.user.model.User
import com.rifqi.trackfunds.core.domain.common.repository.UserSessionProvider
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionProvider: UserSessionProvider,
    @ApplicationContext private val context: Context
) : UserRepository {

    private val TAG = "UserRepository"

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeProfile(): Flow<User?> {
        return sessionProvider.getUidFlow().flatMapLatest { uid ->
            if (uid == null) {
                flowOf(null)
            } else {
                userDao.getProfile(uid).map { entity ->
                    entity?.toDomain()
                }
            }
        }
    }

    override suspend fun saveProfile(user: User, newImageUri: Uri?): Result<Unit> {
        return runCatching {
            val uid = sessionProvider.getUid()
            val existingEntity = userDao.getProfile(uid).first()
                ?: throw Exception("User entity not found for update.")

            var finalPhotoUrl = existingEntity.photoUrl

            if (newImageUri != null) {
                finalPhotoUrl = saveImageLocally(newImageUri, uid)
                    ?: throw IOException("Failed to save image locally.")
            }

            val updatedEntity = existingEntity.copy(
                fullName = user.fullName,
                photoUrl = finalPhotoUrl,
                phoneNumber = user.phoneNumber,
                birthdate = user.birthdate
            )
            userDao.upsert(updatedEntity)
        }
    }

    override suspend fun deleteProfile(): Result<Unit> {
        return runCatching {
            val uid = sessionProvider.getUid()

            // 1. Hapus data pengguna dari database lokal (Room)
            userDao.deleteUserById(uid)

            // 2. Hapus file lokal terkait (misalnya, foto profil)
            val directory = File(context.filesDir, "profile_images")
            val profileImage = File(directory, "$uid-profile.jpg")
            if (profileImage.exists()) {
                profileImage.delete()
            }
        }
    }

    private fun saveImageLocally(sourceUri: Uri, uid: String): String? {
        return try {
            // Dapatkan input stream dari URI sumber
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null

            // Buat direktori tujuan di dalam penyimpanan internal aplikasi
            val directory = File(context.filesDir, "profile_images")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Buat file tujuan
            val destinationFile = File(directory, "$uid-profile.jpg")
            val outputStream = FileOutputStream(destinationFile)

            // Salin data dari input ke output stream
            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            // Kembalikan path absolut dari file yang baru disimpan
            destinationFile.absolutePath
        } catch (e: IOException) {
            Log.e(TAG, "Failed to save local image", e)
            null
        }
    }
}