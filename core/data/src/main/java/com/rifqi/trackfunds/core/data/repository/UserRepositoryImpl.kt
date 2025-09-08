package com.rifqi.trackfunds.core.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.rifqi.trackfunds.core.data.local.dao.UserDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.user.model.User
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val session: UserSessionRepository,
    @ApplicationContext private val context: Context
) : UserRepository {

    override fun observeProfile(): Flow<User?> {
        return session.userUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) {
                flowOf(null)
            } else {
                userDao.getProfile(uid).map { it?.toDomain() }
            }
        }.distinctUntilChanged()
    }

    override suspend fun saveProfile(user: User, newImageUri: Uri?): Result<Unit> = try {
        val uid = session.requireActiveUserId()

        val existingEntity = userDao.getProfile(uid).first()
            ?: throw IllegalStateException("User entity not found for update.")

        val finalPhotoUrl = if (newImageUri != null) {
            saveImageLocally(newImageUri, uid)
                ?: throw IOException("Failed to save image locally.")
        } else {
            existingEntity.photoUrl
        }

        val updatedEntity = existingEntity.copy(
            fullName = user.fullName,
            photoUrl = finalPhotoUrl,
            phoneNumber = user.phoneNumber,
            birthdate = user.birthdate
        )

        userDao.upsert(updatedEntity)
        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun deleteProfile(): Result<Unit> = try {
        val uid = session.requireActiveUserId()

        // 1) Hapus dari DB
        userDao.deleteUserById(uid)

        // 2) Hapus file lokal di thread I/O
        withContext(kotlinx.coroutines.Dispatchers.IO) {
            val directory = File(context.filesDir, "profile_images")
            val profileImage = File(directory, "$uid-profile.jpg")
            if (profileImage.exists()) {
                profileImage.delete()
            }
        }

        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun saveImageLocally(sourceUri: Uri, uid: String): String? =
        withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                context.contentResolver.openInputStream(sourceUri)?.use { input ->
                    val directory = File(context.filesDir, "profile_images")
                    if (!directory.exists()) directory.mkdirs()

                    val destinationFile = File(directory, "$uid-profile.jpg")
                    FileOutputStream(destinationFile).use { output ->
                        input.copyTo(output)
                    }
                    destinationFile.absolutePath
                }
            } catch (e: IOException) {
                Log.e(TAG, "Failed to save local image", e)
                null
            }
        }

    companion object {
        private const val TAG = "UserRepository"
    }
}
