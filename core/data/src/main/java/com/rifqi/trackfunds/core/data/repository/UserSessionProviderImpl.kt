package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.domain.common.exception.AuthRequiredException
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserSessionProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSessionProviderImpl @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : UserSessionProvider {

    override fun getUidFlow(): Flow<String?> {
        return userPreferencesRepository.userUid
    }

    override suspend fun getUid(): String {
        // Ambil UID sekali, jika null, lempar exception khusus
        return userPreferencesRepository.userUid.first()
            ?: throw AuthRequiredException()
    }
}