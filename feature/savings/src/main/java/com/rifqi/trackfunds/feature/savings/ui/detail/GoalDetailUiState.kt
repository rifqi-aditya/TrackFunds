package com.rifqi.trackfunds.feature.savings.ui.detail

// --- State: Data yang akan ditampilkan ---
data class GoalDetailUiState(
    val isLoading: Boolean = true,
    val goalName: String = "",
    val iconIdentifier: String = "",
    val progress: Float = 0f,
    val savedAmountFormatted: String = "",
    val targetAmountFormatted: String = "",
    val stats: List<StatItem> = emptyList(),
    val transactions: List<GoalTransactionUiModel> = emptyList(),
    val showDeleteConfirmDialog: Boolean = false
)

data class StatItem(
    val label: String,
    val value: String
)