package com.rifqi.trackfunds.feature.home.ui.settings

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme

data class SettingsUiState(
    val isLoading: Boolean = true,
    val userName: String = "",
    val userEmail: String = "",
    val userPhotoUrl: String? = null,

    val appTheme: AppTheme = AppTheme.SYSTEM,
    val showThemePickerDialog: Boolean = false,

    val localeTag: String = "en",
    val showLanguagePickerDialog: Boolean = false,

    val showLogoutConfirmDialog: Boolean = false,
    val appVersion: String = ""
)