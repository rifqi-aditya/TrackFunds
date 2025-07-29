package com.rifqi.trackfunds.feature.savings.ui.detail

sealed interface SavingsDetailEvent {
    data object AddFundsClicked : SavingsDetailEvent
    data object EditGoalClicked : SavingsDetailEvent
    data object DeleteGoalClicked : SavingsDetailEvent

    // Tambahkan event ini untuk dialog
    data object ConfirmDeleteClicked : SavingsDetailEvent
    data object DismissDeleteDialog : SavingsDetailEvent
}