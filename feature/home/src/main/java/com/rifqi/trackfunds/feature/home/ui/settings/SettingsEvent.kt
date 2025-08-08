package com.rifqi.trackfunds.feature.home.ui.settings

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme

sealed interface SettingsEvent {
    data object ProfileClicked : SettingsEvent
    data object ManageAccountsClicked : SettingsEvent
    data object ManageCategoriesClicked : SettingsEvent
    data object ThemeItemClicked : SettingsEvent
    data class ThemeChanged(val theme: AppTheme) : SettingsEvent
    data object SecurityClicked : SettingsEvent
    data object LogoutClicked : SettingsEvent
    data object ConfirmLogoutClicked : SettingsEvent
    data object DismissLogoutDialog : SettingsEvent
    data object DismissThemeDialog : SettingsEvent
}