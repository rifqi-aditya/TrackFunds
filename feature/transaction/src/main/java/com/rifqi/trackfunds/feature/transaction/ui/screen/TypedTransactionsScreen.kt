package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.screen.TransactionHistoryContent
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.TypedTransactionsViewModel

@Composable
fun TypedTransactionsScreen(
    viewModel: TypedTransactionsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEditTransaction: (String) -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    TransactionHistoryContent(
        title = if (viewModel.transactionType == TransactionType.INCOME) "All Income" else "All Expenses",
        isLoading = uiState.isLoading,
        transactions = uiState.transactions,
        dateRange = uiState.dateRange,
        error = uiState.error,
        onNavigateBack = onNavigateBack,
        onTransactionClick = onNavigateToEditTransaction,
        onAddTransactionClick = onNavigateToAddTransaction,
        onSearchClick = { /* TODO */ },
        onCalendarClick = { /* TODO */ },
    )
}