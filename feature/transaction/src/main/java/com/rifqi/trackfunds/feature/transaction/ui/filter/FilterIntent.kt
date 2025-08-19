package com.rifqi.trackfunds.feature.transaction.ui.filter

import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import java.time.LocalDate

sealed interface FilterIntent {
    data object ResetClicked : FilterIntent
    data object CancelClicked : FilterIntent
    data object ApplyClicked : FilterIntent

    data class CategoryToggled(val id: String) : FilterIntent
    data class AccountToggled(val id: String) : FilterIntent
    data class TypeToggled(val type: TransactionType) : FilterIntent

    data class DateOptionSelected(val option: DateRangeOption) : FilterIntent
    data class CustomDateChanged(val start: LocalDate, val end: LocalDate) : FilterIntent

    data object ToggleShowAllCategories : FilterIntent
}