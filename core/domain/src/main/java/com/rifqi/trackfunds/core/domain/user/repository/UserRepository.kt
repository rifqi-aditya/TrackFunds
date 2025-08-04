package com.rifqi.trackfunds.core.domain.user.repository

import android.net.Uri
import com.rifqi.trackfunds.core.domain.common.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUser(): Flow<User?>
    suspend fun createOrUpdateProfile(
        user: User,
        imageUri: Uri?
    ): Result<Unit>
}