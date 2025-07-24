package com.rifqi.trackfunds.feature.savings.ui.state

import java.time.LocalDate

data class AddEditSavingsGoalState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val showIconPicker: Boolean = false,
    val showDatePicker: Boolean = false,

    // Form Fields
    val goalName: String = "",
    val goalNameError: String? = null,
    val targetAmount: String = "",
    val targetAmountError: String? = null,
    val targetDate: LocalDate? = null,
    val targetDateError: String? = null,
    val iconIdentifier: String = "",
    val iconError: String? = null,
)