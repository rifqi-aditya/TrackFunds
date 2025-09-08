package com.rifqi.trackfunds.feature.home.ui.settings

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme

sealed interface SettingsEvent {
    data object ProfileClicked : SettingsEvent
    data object ManageAccountsClicked : SettingsEvent
    data object ManageCategoriesClicked : SettingsEvent
    data object SecurityClicked : SettingsEvent

    data object ThemeItemClicked : SettingsEvent
    data object DismissThemeDialog : SettingsEvent
    data class ThemeChanged(val theme: AppTheme) : SettingsEvent

    data object LanguageItemClicked : SettingsEvent
    data object DismissLanguageDialog : SettingsEvent
    data class LanguageChanged(val tag: String) : SettingsEvent

    data object LogoutClicked : SettingsEvent
    data object DismissLogoutDialog : SettingsEvent
    data object ConfirmLogoutClicked : SettingsEvent
}