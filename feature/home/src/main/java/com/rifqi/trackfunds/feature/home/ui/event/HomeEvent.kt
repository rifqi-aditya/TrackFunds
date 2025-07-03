package com.rifqi.trackfunds.feature.home.ui.event

import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.feature.home.ui.state.HomeCategorySummaryItem
import java.time.YearMonth

sealed interface HomeEvent {
    // Aksi Navigasi
    data object NotificationsClicked : HomeEvent
    data object AllTransactionsClicked : HomeEvent
    data object BudgetsScreenClicked : HomeEvent
    data object AddTransactionManuallyClicked : HomeEvent
    data object ScanReceiptClicked : HomeEvent
    data class TypedTransactionsClicked(val type: TransactionType) : HomeEvent
    data class CategorySummaryClicked(val item: HomeCategorySummaryItem) : HomeEvent
    data class TransactionClicked(val transactionId: String) : HomeEvent

    // Aksi pada State
    data object ChangePeriodClicked : HomeEvent
    data class PeriodChanged(val newPeriod: YearMonth) : HomeEvent
    data object DialogDismissed : HomeEvent
    data object AddActionDialogDismissed : HomeEvent
    data object FabClicked : HomeEvent
}