package com.rifqi.trackfunds.feature.transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.feature.transaction.ui.model.TransactionHistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

// --- DATA DUMMY (dengan BigDecimal) ---
val DUMMY_ALL_TRANSACTIONS = listOf(
    TransactionItem(
        id = "t1",
        description = "Mbanking initial balance",
        amount = BigDecimal("200000.00"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(1).withHour(9),
        categoryId = "init",
        categoryName = "Initial Balance",
        iconIdentifier = "stack_of_money",
        accountId = "acc2"
    ),
    TransactionItem(
        id = "t2",
        description = "Wallet initial balance",
        amount = BigDecimal("100000.00"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(1).withHour(9),
        categoryId = "init",
        categoryName = "Initial Balance",
        iconIdentifier = "wallet",
        accountId = "acc1"
    ),
    TransactionItem(
        id = "t3",
        description = "Uang Jajan",
        amount = BigDecimal("30000.00"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().withHour(11).withMinute(56),
        categoryId = "food",
        categoryName = "Makanan",
        iconIdentifier = "restaurant",
        accountId = "acc1"
    ),
    TransactionItem(
        id = "t4",
        description = "Liquid vape",
        amount = BigDecimal("20000.00"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().withHour(11).withMinute(55),
        categoryId = "shop",
        categoryName = "Belanja",
        iconIdentifier = "shopping_cart",
        accountId = "acc1"
    ),
    TransactionItem(
        id = "t5",
        description = "Healthcare",
        amount = BigDecimal("25555.00"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().withHour(15).withMinute(58),
        categoryId = "health",
        categoryName = "Kesehatan",
        iconIdentifier = "medical_doctor",
        accountId = "acc2"
    )
)

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    // private val getTransactionsUseCase: GetTransactionsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionHistoryUiState(isLoading = true))
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    private val filterType: String = savedStateHandle.get<String>(ARG_TRANSACTION_TYPE) ?: "ALL"

    init {
        loadTransactions()
    }

    private fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000)

            val allTransactions = DUMMY_ALL_TRANSACTIONS

            val filteredTransactions: List<TransactionItem>
            val title: String
            val summaryAmount: BigDecimal

            when (filterType.uppercase()) {
                "EXPENSE" -> {
                    title = "Expenses"
                    filteredTransactions =
                        allTransactions.filter { it.type == TransactionType.EXPENSE }
                    // Menggunakan reduce untuk menjumlahkan BigDecimal
                    summaryAmount = filteredTransactions.fold(BigDecimal.ZERO) { acc, transaction ->
                        acc.add(transaction.amount)
                    }
                }

                "INCOME" -> {
                    title = "Income"
                    filteredTransactions =
                        allTransactions.filter { it.type == TransactionType.INCOME }
                    summaryAmount = filteredTransactions.fold(BigDecimal.ZERO) { acc, transaction ->
                        acc.add(transaction.amount)
                    }
                }

                else -> { // "ALL" atau kasus lainnya
                    title = "Transactions"
                    filteredTransactions = allTransactions
                    val totalIncome = allTransactions
                        .filter { it.type == TransactionType.INCOME }
                        .fold(BigDecimal.ZERO) { acc, transaction -> acc.add(transaction.amount) }
                    val totalExpense = allTransactions
                        .filter { it.type == TransactionType.EXPENSE }
                        .fold(BigDecimal.ZERO) { acc, transaction -> acc.add(transaction.amount) }
                    summaryAmount = totalIncome.subtract(totalExpense) // Gunakan subtract
                }
            }

            _uiState.update {
                it.copy(
                    isLoading = false,
                    title = title,
                    dateRange = "01 Jun 25 - 30 Jun 25",
                    summaryAmount = summaryAmount,
                    transactions = filteredTransactions.sortedByDescending { tx -> tx.date }
                )
            }
        }
    }

    companion object {
        const val ARG_TRANSACTION_TYPE = "transaction_type"
    }
}