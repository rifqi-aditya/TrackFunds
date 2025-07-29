package com.rifqi.trackfunds.feature.savings.ui.event

sealed interface SavingsGoalEvent {
    data object AddGoalClicked : SavingsGoalEvent
    data class GoalClicked(val goalId: String) : SavingsGoalEvent
    data object NavigateBackClicked : SavingsGoalEvent
}