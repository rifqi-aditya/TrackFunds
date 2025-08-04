package com.rifqi.trackfunds.core.domain.settings.usecase

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import com.rifqi.trackfunds.core.domain.settings.repository.SettingsRepository
import javax.inject.Inject

interface SetThemePreferenceUseCase {
    suspend operator fun invoke(theme: AppTheme)
}

class SetThemePreferenceUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository
) : SetThemePreferenceUseCase {
    override suspend operator fun invoke(theme: AppTheme) {
        settingsRepository.setTheme(theme)
    }
}