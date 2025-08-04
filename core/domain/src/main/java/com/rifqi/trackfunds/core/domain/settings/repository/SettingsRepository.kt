package com.rifqi.trackfunds.core.domain.settings.repository

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getTheme(): Flow<AppTheme>
    suspend fun setTheme(theme: AppTheme)
}