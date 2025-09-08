package com.rifqi.trackfunds.core.domain.settings.usecase

import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveActiveUserIdUseCase { operator fun invoke(): Flow<String?> }
class ObserveActiveUserIdUseCaseImpl @Inject constructor(
    private val prefs: AppPrefsRepository
) : ObserveActiveUserIdUseCase {
    override fun invoke(): Flow<String?> = prefs.activeUserId
}
