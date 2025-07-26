package com.rifqi.trackfunds.core.domain.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(): Flow<UserModel?>
    suspend fun createOrUpdateProfile(
        userModel: UserModel,
        imageUri: Uri?
    ): Result<Unit>
}