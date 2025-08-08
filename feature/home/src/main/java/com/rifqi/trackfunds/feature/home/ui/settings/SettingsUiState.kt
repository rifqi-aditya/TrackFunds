package com.rifqi.trackfunds.feature.home.ui.settings

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme

data class SettingsUiState(
    val isLoading: Boolean = true,
    val userName: String = "User",
    val userEmail: String = "",
    val userPhotoUrl: String? = null,
    val appTheme: AppTheme = AppTheme.SYSTEM,
    val appVersion: String = "1.0.0",
    val showLogoutConfirmDialog: Boolean = false,
    val showThemePickerDialog: Boolean = false
)