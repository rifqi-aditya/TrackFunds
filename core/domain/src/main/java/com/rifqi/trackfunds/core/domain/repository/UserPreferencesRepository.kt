package com.rifqi.trackfunds.core.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userUid: Flow<String?>
    suspend fun saveUserUid(uid: String)
    suspend fun clearUserUid()
}