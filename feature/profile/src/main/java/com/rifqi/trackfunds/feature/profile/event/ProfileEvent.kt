package com.rifqi.trackfunds.feature.profile.event

sealed interface ProfileEvent {
    data object SettingsClicked : ProfileEvent
    data object ManageAccountsClicked : ProfileEvent
    data object ManageCategoriesClicked : ProfileEvent
    data object LogoutClicked : ProfileEvent
    data class DarkModeToggled(val isEnabled: Boolean) : ProfileEvent
}