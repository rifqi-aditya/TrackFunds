package com.rifqi.trackfunds.feature.savings.ui.detail

sealed interface GoalDetailEvent {
    data object OnNavigateBack : GoalDetailEvent
    data object OnEditClicked : GoalDetailEvent
    data object OnDeleteClicked : GoalDetailEvent
    data object OnConfirmDelete : GoalDetailEvent
    data object OnDismissDeleteDialog : GoalDetailEvent
    data object OnAddFundsClicked : GoalDetailEvent
}