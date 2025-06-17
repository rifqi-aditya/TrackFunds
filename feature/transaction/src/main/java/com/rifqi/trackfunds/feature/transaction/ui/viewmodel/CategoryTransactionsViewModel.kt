package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByCategoryIdUseCase
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.ui.util.formatDateRangeToString
import com.rifqi.trackfunds.core.ui.util.getCurrentDateRangePair
import com.rifqi.trackfunds.feature.transaction.ui.model.TransactionHistoryUiState
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
class CategoryTransactionsViewModel @Inject constructor(
    private val getTransactionsByCategoryIdUseCase: GetTransactionsByCategoryIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ambil argumen navigasi (categoryId & categoryName) dengan aman
    private val args: CategoryTransactions = savedStateHandle.toRoute()
    val categoryName: String = args.categoryName

    private val _uiState = MutableStateFlow(TransactionHistoryUiState(isLoading = true))
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            // Untuk sementara kita pakai rentang tanggal bulan ini.
            // Anda bisa membuatnya lebih dinamis nanti.
            val dateRange = getCurrentDateRangePair()
            val startDate = dateRange.first.atStartOfDay()
            val endDate = dateRange.second.atTime(23, 59, 59)

            // Panggil use case dengan categoryId dari argumen navigasi
            getTransactionsByCategoryIdUseCase(args.categoryId, startDate, endDate)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
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