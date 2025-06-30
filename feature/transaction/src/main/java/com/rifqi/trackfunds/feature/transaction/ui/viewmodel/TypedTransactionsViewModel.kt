package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByTypeUseCase
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.core.ui.util.formatDateRangeToString
import com.rifqi.trackfunds.core.ui.util.getCurrentDateRangePair
import com.rifqi.trackfunds.feature.transaction.ui.state.TransactionHistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TypedTransactionsViewModel @Inject constructor(
    private val getTransactionsByTypeUseCase: GetTransactionsByTypeUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val args: TypedTransactions = savedStateHandle.toRoute()
    val transactionType: TransactionType = args.transactionType

    private val _uiState = MutableStateFlow(TransactionHistoryUiState(isLoading = true))
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            val dateRange = getCurrentDateRangePair()
            val startDate = dateRange.first.atStartOfDay()
            val endDate = dateRange.second.atTime(23, 59, 59)

            getTransactionsByTypeUseCase(args.transactionType, startDate, endDate)
                .onStart { _uiState.update { it.copy(isLoading = true, error = null) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { transactions ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            transactions = transactions,
                            dateRange = formatDateRangeToString(dateRange)
                        )
                    }
                }
        }
    }
}