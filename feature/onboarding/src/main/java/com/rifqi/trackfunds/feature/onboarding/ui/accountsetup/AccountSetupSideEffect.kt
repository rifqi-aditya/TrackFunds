package com.rifqi.trackfunds.feature.onboarding.ui.accountsetup

sealed interface AccountSetupSideEffect {
    data class ShowMessage(val message: String) : AccountSetupSideEffect
    data object NavigateHome : AccountSetupSideEffect
}