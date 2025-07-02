package com.rifqi.account.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.account.ui.model.AccountTimelineUiState
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
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

// --- DATA DUMMY (nantinya dihapus) ---
private val DUMMY_TIMELINE_TRANSACTIONS = listOf(
    TransactionItem(
        id = "t1",
        note = "Mbanking initial balance",
        amount = BigDecimal("210000.0"),
        type = TransactionType.INCOME,
        date = LocalDateTime.now().minusDays(1).withHour(9),
        categoryId = "init",
        categoryName = "Initial",
        categoryIconIdentifier = "cash",
        accountId = "acc2",
        accountName = "acc2"
    ),
    TransactionItem(
        id = "t2",
        note = "Uang Jajan",
        amount = BigDecimal("210000.0"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().withHour(11).withMinute(56),
        categoryId = "food",
        categoryName = "Makanan",
        categoryIconIdentifier = "restaurant",
        accountId = "acc2",
        accountName = "acc2"
    ),
    TransactionItem(
        id = "t3",
        note = "Liquid vape",
        amount = BigDecimal("210000.0"),
        type = TransactionType.EXPENSE,
        date = LocalDateTime.now().withHour(11).withMinute(55),
        categoryId = "shop",
        categoryName = "Belanja",
        categoryIconIdentifier = "shopping_cart",
        accountId = "acc2",
        accountName = "acc2"
    )
)

@HiltViewModel
class AccountTimelineViewModel @Inject constructor(
    // private val getAccountDetailsUseCase: GetAccountDetailsUseCase,
    // private val getTransactionsForAccountUseCase: GetTransactionsForAccountUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ambil ID akun dari argumen navigasi
    private val accountId: String = savedStateHandle.get<String>(ARG_WALLET_ID) ?: ""

    private val _uiState = MutableStateFlow(AccountTimelineUiState(isLoading = true))
    val uiState: StateFlow<AccountTimelineUiState> = _uiState.asStateFlow()

    init {
        loadAccountDetails()
    }

    private fun loadAccountDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000) // Simulasi loading

            // TODO: Panggil use case untuk mendapatkan detail akun dan transaksinya berdasarkan 'accountId'

            // Untuk sekarang, gunakan data dummy
            val grouped = DUMMY_TIMELINE_TRANSACTIONS
                .sortedByDescending { it.date }
                .groupBy { it.date.toLocalDate() } // Mengelompokkan transaksi berdasarkan tanggal

            _uiState.update {
                it.copy(
                    isLoading = false,
                    accountName = "Mbanking",
                    currentBalance = BigDecimal("210000.0"), // Ini seharusnya hasil kalkulasi
                    groupedTransactions = grouped
                )
            }
        }
    }

    fun onTransferClicked() {
        // TODO: Navigasi ke halaman transfer
        println("Transfer button clicked for account: $accountId")
    }

    companion object {
        const val ARG_WALLET_ID = "wallet_id"
    }
}

