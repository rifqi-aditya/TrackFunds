package com.rifqi.trackfunds.feature.onboarding.ui.accountsetup

data class AccountSetupUiState(
    val availableIcons: List<String> = emptyList(),
    val selectedIcon: String? = null,
    val name: String = "",
    val initialBalance: String = "0",
    val isIconSheetVisible: Boolean = false,
    val isSaving: Boolean = false,
    val nameError: String? = null,
    val balanceError: String? = null
) {
    val canSubmit: Boolean get() = !isSaving && name.isNotBlank()
}