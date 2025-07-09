package com.rifqi.trackfunds.feature.home.ui.event

sealed interface HomeEvent {
    // Aksi Navigasi
    data object NotificationsClicked : HomeEvent
    data object AllTransactionsClicked : HomeEvent
    data object AllBudgetsClicked : HomeEvent
    data object AddTransactionManuallyClicked : HomeEvent
    data object ScanReceiptClicked : HomeEvent
    data object BalanceClicked : HomeEvent
    data object SavingsClicked : HomeEvent
    data object AccountsClicked : HomeEvent
    data class TransactionClicked(val transactionId: String) : HomeEvent

    // Aksi pada State
    data object AddActionDialogDismissed : HomeEvent
    data object FabClicked : HomeEvent
}