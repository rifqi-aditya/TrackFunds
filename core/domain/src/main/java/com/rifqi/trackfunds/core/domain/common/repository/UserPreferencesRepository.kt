package com.rifqi.trackfunds.core.domain.common.repository

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userUid: Flow<String?>
    suspend fun saveUserUid(uid: String)
    suspend fun clearUserUid()
}