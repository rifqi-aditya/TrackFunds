package com.rifqi.trackfunds.feature.transaction.ui.list

import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.feature.transaction.ui.model.ActiveFilterChip

sealed interface TransactionListEvent {
    data class SearchQueryChanged(val query: String) : TransactionListEvent
    data object FilterButtonClicked : TransactionListEvent
    data class FilterApplied(val newFilter: TransactionFilter) : TransactionListEvent
    data class RemoveFilterChip(val chip: ActiveFilterChip) : TransactionListEvent
    data class TransactionClicked(val transactionId: String) : TransactionListEvent
}