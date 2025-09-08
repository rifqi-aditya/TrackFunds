package com.rifqi.trackfunds.core.domain.common.repository

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface AppPrefsRepository {
    val activeUserId: Flow<String?>
    val appTheme: StateFlow<AppTheme>
    val localeTag: StateFlow<String>

    suspend fun setActiveUserId(id: String)
    suspend fun clearActiveUserId()

    fun setTheme(theme: AppTheme)
    fun setLocale(tag: String)

    suspend fun requireActiveUserId(): String
}