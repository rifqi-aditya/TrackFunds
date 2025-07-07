package com.rifqi.trackfunds.feature.savings.ui.event

sealed interface SavingsEvent {
    data object AddNewGoalClicked : SavingsEvent
    data class GoalClicked(val goalId: String) : SavingsEvent
}