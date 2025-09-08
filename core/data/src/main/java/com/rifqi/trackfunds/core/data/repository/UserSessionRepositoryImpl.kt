package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserSessionRepositoryImpl @Inject constructor(
    private val appPrefsRepository: AppPrefsRepository // existing prefs repo
) : UserSessionRepository {

    override fun userUidFlow(): Flow<String?> = appPrefsRepository.activeUserId
}