package com.rifqi.trackfunds.feature.home.ui.event

sealed interface HomeEvent {
    // Aksi Navigasi
    data object NotificationsClicked : HomeEvent
    data object ProfileClicked : HomeEvent
    data object AllTransactionsClicked : HomeEvent
    data object AllBudgetsClicked : HomeEvent
    data object BalanceClicked : HomeEvent
    data object SavingsClicked : HomeEvent
    data object AccountsClicked : HomeEvent
    data class TransactionClicked(val transactionId: String) : HomeEvent

    data class TabSelected(val index: Int) : HomeEvent
}