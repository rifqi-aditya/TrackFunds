package com.rifqi.trackfunds.ui

import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import com.rifqi.trackfunds.core.navigation.api.AppScreen

data class MainUiState(
    val theme: AppTheme = AppTheme.SYSTEM,
    val isAddActionDialogVisible: Boolean = false,
    val startDestination: AppScreen? = null,
)