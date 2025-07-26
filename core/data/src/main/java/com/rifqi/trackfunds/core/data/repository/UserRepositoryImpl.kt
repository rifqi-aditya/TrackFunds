package com.rifqi.trackfunds.core.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.rifqi.trackfunds.core.data.local.dao.UserDao
import com.rifqi.trackfunds.core.data.local.entity.UserEntity
import com.rifqi.trackfunds.core.data.mapper.toDomainModel
import com.rifqi.trackfunds.core.domain.model.UserModel
import com.rifqi.trackfunds.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val userDao: UserDao,
    private val storage: FirebaseStorage
) : UserRepository {

    override fun getProfile(): Flow<UserModel?> {
        val uid = auth.currentUser?.uid ?: return flowOf(null)
        return userDao.getProfile(uid).map { entity ->
            entity?.toDomainModel()
        }
    }

    override suspend fun createOrUpdateProfile(userModel: UserModel, imageUri: Uri?): Result<Unit> {
        val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not logged in."))

        return try {
            var finalPhotoUrl = userModel.photoUrl

            // Logika upload foto ke Firebase Storage tidak berubah
            if (imageUri != null) {
                val storageRef = storage.reference.child("profile_images/$uid/profile.jpg")
                storageRef.putFile(imageUri).await()
                finalPhotoUrl = storageRef.downloadUrl.await().toString()
            }

            // Buat entity untuk disimpan ke Room
            val userEntity = UserEntity(
                uid = uid,
                email = userModel.email ?: "",
                username = userModel.username,
                fullName = userModel.fullName,
                photoUrl = finalPhotoUrl,
                phoneNumber = userModel.phoneNumber,
                gender = userModel.gender,
                birthdate = userModel.birthdate
            )

            // Simpan ke database lokal
            userDao.insertOrUpdateProfile(userEntity)

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}