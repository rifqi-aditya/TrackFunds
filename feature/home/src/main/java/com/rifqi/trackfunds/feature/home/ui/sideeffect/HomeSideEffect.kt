package com.rifqi.trackfunds.feature.home.ui.sideeffect

sealed interface HomeSideEffect {
    data object NavigateBack : HomeSideEffect
    data object NavigateToNotifications : HomeSideEffect
    data object NavigateToProfile : HomeSideEffect
    data object NavigateToTransactions : HomeSideEffect
    data object NavigateToBudgets : HomeSideEffect
    data object NavigateToAccounts : HomeSideEffect
    data class NavigateToTransactionDetails(val transactionId: String) : HomeSideEffect
    data class ShowSnackbar(val message: String) : HomeSideEffect

}