package com.rifqi.trackfunds.ui

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme

data class MainUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val isAddActionDialogVisible: Boolean = false
)