package com.rifqi.add_transaction.ui.model

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.LocalDate

// Data class untuk state UI halaman Tambah Transaksi
data class AddTransactionUiState(
    val amount: String = "",
    val selectedTransactionType: TransactionType = TransactionType.EXPENSE,
    val selectedAccount: AccountItem? = null,
    val selectedCategory: CategoryItem? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val notes: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isTransactionSaved: Boolean = false,
    val showDatePicker: Boolean = false
)

// Model sederhana untuk tampilan Akun di UI (bisa lebih kompleks sesuai kebutuhan)
data class AccountDisplayItem(
    val id: String,
    val name: String,
    val iconIdentifier: String? // Identifier untuk ikon akun
)