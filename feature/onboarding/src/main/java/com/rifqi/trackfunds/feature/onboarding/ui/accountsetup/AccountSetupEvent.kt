package com.rifqi.trackfunds.feature.onboarding.ui.accountsetup

sealed interface AccountSetupEvent {
    data object IconPickerOpened : AccountSetupEvent
    data object IconPickerDismissed : AccountSetupEvent
    data class IconSelected(val key: String) : AccountSetupEvent

    data class NameChanged(val value: String) : AccountSetupEvent
    data class InitialBalanceChanged(val value: String) : AccountSetupEvent

    data object SubmitClicked : AccountSetupEvent
}