package com.rifqi.trackfunds.feature.savings.ui.goals_list

sealed interface SavingsGoalEvent {
    data object AddGoalClicked : SavingsGoalEvent
    data class GoalClicked(val goalId: String) : SavingsGoalEvent
    data object NavigateBackClicked : SavingsGoalEvent
}