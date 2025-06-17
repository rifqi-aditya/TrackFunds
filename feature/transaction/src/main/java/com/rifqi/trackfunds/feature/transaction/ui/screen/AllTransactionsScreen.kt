package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.screen.TransactionHistoryContent
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.AllTransactionsViewModel

@Composable
fun AllTransactionsScreen(
    viewModel: AllTransactionsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToTransactionDetail: (String) -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    TransactionHistoryContent(
        title = "All Transactions",
        isLoading = uiState.isLoading,
        transactions = uiState.transactions,
        dateRange = uiState.dateRange,
        error = uiState.error,
        onNavigateBack = onNavigateBack,
        onTransactionClick = onNavigateToTransactionDetail,
        onAddTransactionClick = onNavigateToAddTransaction,
        onSearchClick = { /* TODO */ },
        onCalendarClick = { /* TODO */ },
    )
}