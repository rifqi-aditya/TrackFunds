package com.rifqi.trackfunds.core.domain.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(): Flow<User?>
    suspend fun createOrUpdateProfile(
        user: User,
        imageUri: Uri?
    ): Result<Unit>
}