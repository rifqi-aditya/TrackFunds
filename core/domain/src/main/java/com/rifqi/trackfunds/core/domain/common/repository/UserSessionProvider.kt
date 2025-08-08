package com.rifqi.trackfunds.core.domain.common.repository

import kotlinx.coroutines.flow.Flow

interface UserSessionProvider {
    fun getUidFlow(): Flow<String?>
    suspend fun getUid(): String
}