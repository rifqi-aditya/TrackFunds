package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.feature.transaction.ui.model.TransactionListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

// --- DATA DUMMY UNTUK PREVIEW DAN DEVELOPMENT AWAL ---
// Nantinya ini akan dihapus dan diganti dengan data dari Use Case
// Data dummy untuk UI, nantinya akan diganti data dari Use Case
val dummyTransactionList = listOf(
    TransactionItem(
        id = UUID.randomUUID().toString(),
        description = "Mbanking initial balance",
        amount = BigDecimal("12234"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(3).withHour(11).withMinute(24),
        categoryId = "cat_initial_mbanking",
        categoryName = "Initial Balance",
        iconIdentifier = "ic_default_category", // Ganti dengan identifier yang sesuai
        accountId = "acc2"
    ),
    TransactionItem(
        id = UUID.randomUUID().toString(),
        description = "Wallet initial balance",
        amount = BigDecimal("12234"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(3).withHour(11).withMinute(24),
        categoryId = "cat_initial_wallet",
        categoryName = "Initial Balance",
        iconIdentifier = "ic_default_category",
        accountId = "acc1"
    ),
    TransactionItem(
        id = UUID.randomUUID().toString(),
        description = "Uang Jajan",
        amount = BigDecimal("12234"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().minusDays(2).withHour(11).withMinute(56),
        categoryId = "cat_food",
        categoryName = "Makanan",
        iconIdentifier = "restaurant",
        accountId = "acc1"
    ),
    TransactionItem(
        id = UUID.randomUUID().toString(),
        description = "Liquid vape",
        amount = BigDecimal("12234"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().minusDays(1).withHour(11).withMinute(55),
        categoryId = "cat_shopping",
        categoryName = "Belanja",
        iconIdentifier = "shopping_cart",
        accountId = "acc1"
    )
)

@HiltViewModel
class TransactionListViewModel @Inject constructor(
    // private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionListUiState(isLoading = true))
    val uiState: StateFlow<TransactionListUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1500) // Simulasi loading

            // TODO: Nantinya panggil getTransactionsUseCase()
            val elapsedAmount = dummyTransactionList
                .filter { it.type == TransactionType.EXPENSE }
                .sumOf { it.amount }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    dateRange = "01 Jun 25 - 30 Jun 25",
                    elapsedAmount = elapsedAmount,
                    upcomingAmount = BigDecimal("12234"),
                    transactions = dummyTransactionList
                )
            }
        }
    }
}