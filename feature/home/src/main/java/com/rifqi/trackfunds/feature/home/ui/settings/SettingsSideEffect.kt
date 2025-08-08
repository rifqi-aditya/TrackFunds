package com.rifqi.trackfunds.feature.home.ui.settings

sealed interface SettingsSideEffect {
    data object NavigateToProfile : SettingsSideEffect
    data object NavigateToManageAccounts : SettingsSideEffect
    data object NavigateToManageCategories : SettingsSideEffect
    data object NavigateToSecurity : SettingsSideEffect
    data object NavigateToLogin : SettingsSideEffect
}