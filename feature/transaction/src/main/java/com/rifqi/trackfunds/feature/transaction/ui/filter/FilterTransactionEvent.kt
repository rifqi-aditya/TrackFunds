package com.rifqi.trackfunds.feature.transaction.ui.filter

import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import java.time.LocalDate

/**
 * Represents all user actions on the FilterScreen.
 */
sealed interface FilterTransactionEvent {
    data class CategoryToggled(val categoryId: String) : FilterTransactionEvent
    data class AccountToggled(val accountId: String) : FilterTransactionEvent
    data class DateRangeSelected(val startDate: LocalDate, val endDate: LocalDate) : FilterTransactionEvent
    data object ApplyFilterTransactionClicked : FilterTransactionEvent
    data class DateOptionSelected(val option: DateRangeOption) : FilterTransactionEvent
    data class CustomDateSelected(val startDate: LocalDate, val endDate: LocalDate) : FilterTransactionEvent
    data object ManualDateSelectionClicked : FilterTransactionEvent
    data object DatePickerDismissed : FilterTransactionEvent
    data object ClearFiltersClicked : FilterTransactionEvent
}