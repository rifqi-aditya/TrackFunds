package com.rifqi.trackfunds.core.domain.settings.usecase

import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveAppThemeUseCase {
    operator fun invoke(): Flow<AppTheme>
}

class ObserveAppThemeUseCaseImpl @Inject constructor(
    private val prefs: AppPrefsRepository
) : ObserveAppThemeUseCase {
    override operator fun invoke(): Flow<AppTheme> = prefs.appTheme
}