package com.rifqi.trackfunds.feature.savings.ui.detail

sealed interface GoalDetailSideEffect {
    data object NavigateBack : GoalDetailSideEffect
    data class NavigateToEditGoal(val goalId: String) : GoalDetailSideEffect
    data class ShowSnackbar(val message: String) : GoalDetailSideEffect
}
