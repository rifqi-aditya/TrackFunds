package com.rifqi.trackfunds.core.domain.auth.repository

import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

interface UserSessionRepository {
    fun userUidFlow(): Flow<String?>
    suspend fun requireActiveUserId(): String =
        userUidFlow().first() ?: throw NotAuthenticatedException()
}