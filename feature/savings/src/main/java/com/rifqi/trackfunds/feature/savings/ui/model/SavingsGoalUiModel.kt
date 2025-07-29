package com.rifqi.trackfunds.feature.savings.ui.model

data class SavingsGoalUiModel(
    val id: String,
    val name: String,
    val progress: Float,
    val savedAmountFormatted: String,
    val targetAmountFormatted: String,
    val remainingAmountFormatted: String,
    val targetDateFormatted: String?,
    val iconIdentifier: String
)