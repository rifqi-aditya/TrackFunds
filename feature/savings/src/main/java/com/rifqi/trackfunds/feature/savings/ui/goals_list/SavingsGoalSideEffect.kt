package com.rifqi.trackfunds.feature.savings.ui.goals_list

// Di dalam package ui/event atau package terpisah
sealed interface SavingsGoalSideEffect {
    data object NavigateToAddGoal : SavingsGoalSideEffect
    data object NavigateBack : SavingsGoalSideEffect
    data class NavigateToGoalDetails(val goalId: String) : SavingsGoalSideEffect
    data class ShowSnackbar(val message: String) : SavingsGoalSideEffect
}