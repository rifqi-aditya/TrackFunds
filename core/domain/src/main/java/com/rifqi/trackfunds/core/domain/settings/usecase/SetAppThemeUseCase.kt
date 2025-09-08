package com.rifqi.trackfunds.core.domain.settings.usecase

import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import javax.inject.Inject

interface SetAppThemeUseCase {
    suspend operator fun invoke(theme: AppTheme)
}

class SetAppThemeUseCaseImpl @Inject constructor(
    private val prefs: AppPrefsRepository
) : SetAppThemeUseCase {
    override suspend operator fun invoke(theme: AppTheme) {
        prefs.setTheme(theme)
    }
}