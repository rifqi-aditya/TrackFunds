package com.rifqi.add_transaction.ui.model

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.LocalDate

// Data class untuk state UI halaman Tambah Transaksi
data class AddTransactionUiState(
    val amount: String = "", // Disimpan sebagai String untuk input, validasi di ViewModel
    val selectedTransactionType: TransactionType = TransactionType.EXPENSE, // Default ke Expense
    val selectedAccount: AccountDisplayItem? = null, // Akun yang dipilih (buat data class AccountDisplayItem jika perlu)
    val selectedDate: LocalDate = LocalDate.now(),
    val notes: String = "",
    val selectedCategory: CategoryItem? = null, // Kategori yang dipilih dari layar SelectCategory
    val isLoading: Boolean = false,
    val error: String? = null,
    val transactionSaved: Boolean = false // Flag untuk menandakan transaksi berhasil disimpan
)

// Model sederhana untuk tampilan Akun di UI (bisa lebih kompleks sesuai kebutuhan)
data class AccountDisplayItem(
    val id: String,
    val name: String,
    val iconIdentifier: String? // Identifier untuk ikon akun
)