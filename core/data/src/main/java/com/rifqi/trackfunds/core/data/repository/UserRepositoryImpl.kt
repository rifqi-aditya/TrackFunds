package com.rifqi.trackfunds.core.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.rifqi.trackfunds.core.data.mapper.toUser
import com.rifqi.trackfunds.core.domain.model.User
import com.rifqi.trackfunds.core.domain.repository.UserRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : UserRepository {

    override fun getProfile(): Flow<User?> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val docRef = firestore.collection("users").document(uid)
        val listener = docRef.addSnapshotListener { snapshot, error ->
            if (error != null || snapshot == null) {
                close(error)
                return@addSnapshotListener
            }
            trySend(snapshot.toUser(uid))
        }
        awaitClose { listener.remove() }
    }

    override suspend fun createOrUpdateProfile(
        user: User,
        imageUri: Uri?
    ): Result<Unit> {
        val uid = auth.currentUser?.uid
        return if (uid == null || uid != user.uid) { // Pastikan uid cocok
            Result.failure(Exception("User not logged in or UID mismatch."))
        } else {
            try {
                var photoUrl: String? = user.photoUrl // Ambil URL yang sudah ada

                // 1. Upload foto baru jika ada
                if (imageUri != null) {
                    val storageRef = storage.reference.child("profile_images/$uid/profile.jpg")
                    storageRef.putFile(imageUri).await()
                    photoUrl = storageRef.downloadUrl.await().toString()
                }

                // 2. Siapkan data dari objek User untuk disimpan
                val userProfileMap = mapOf(
                    "fullName" to user.fullName,
                    "username" to user.username,
                    "birthdate" to user.birthdate,
                    "gender" to user.gender,
                    "phoneNumber" to user.phoneNumber,
                    "photoUrl" to photoUrl
                )

                // 3. Simpan/update data ke Firestore
                firestore.collection("users").document(uid)
                    .set(userProfileMap, SetOptions.merge())
                    .await()

                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}