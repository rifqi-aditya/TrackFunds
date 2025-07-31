package com.rifqi.trackfunds.feature.transaction.ui.filter

import com.rifqi.trackfunds.core.common.model.DateRangeOption
import java.time.LocalDate

/**
 * Represents all user actions on the FilterScreen.
 */
sealed interface FilterEvent {
    data class CategoryToggled(val categoryId: String) : FilterEvent
    data class AccountToggled(val accountId: String) : FilterEvent
    data class DateRangeSelected(val startDate: LocalDate, val endDate: LocalDate) : FilterEvent
    data object ApplyFilterClicked : FilterEvent
    data class DateOptionSelected(val option: DateRangeOption) : FilterEvent
    data class CustomDateSelected(val startDate: LocalDate, val endDate: LocalDate) : FilterEvent
    data object ManualDateSelectionClicked : FilterEvent
    data object DatePickerDismissed : FilterEvent
}