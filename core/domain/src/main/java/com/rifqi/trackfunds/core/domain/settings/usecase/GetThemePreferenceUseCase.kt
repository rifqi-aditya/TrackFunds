package com.rifqi.trackfunds.core.domain.settings.usecase

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import com.rifqi.trackfunds.core.domain.settings.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface GetThemePreferenceUseCase {
    operator fun invoke(): Flow<AppTheme>
}

class GetThemePreferenceUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository
) : GetThemePreferenceUseCase {
    override operator fun invoke(): Flow<AppTheme> {
        return settingsRepository.getTheme()
    }
}