package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.screen.TransactionHistoryContent
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.CategoryTransactionsViewModel

@Composable
fun CategoryTransactionsScreen(
    viewModel: CategoryTransactionsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToDetailTransaction: (String) -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    TransactionHistoryContent(
        title = viewModel.categoryName,
        isLoading = uiState.isLoading,
        transactions = uiState.transactions,
        dateRange = uiState.dateRange,
        error = uiState.error,
        onNavigateBack = onNavigateBack,
        onTransactionClick = onNavigateToDetailTransaction,
        onAddTransactionClick = onNavigateToAddTransaction,
        onSearchClick = { /* TODO */ },
        onCalendarClick = { /* TODO */ },
    )
}