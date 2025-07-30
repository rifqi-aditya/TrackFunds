package com.rifqi.trackfunds.feature.savings.ui.detail

data class GoalTransactionUiModel(
    val id: String,
    val description: String,
    val dateFormatted: String,
    val amountFormatted: String,
    val isIncome: Boolean
)
