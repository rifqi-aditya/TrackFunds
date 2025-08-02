package com.rifqi.trackfunds.feature.transaction.ui.detail

import androidx.compose.ui.graphics.Color

// Model baru yang spesifik untuk UI, berisi data yang sudah siap tampil
data class TransactionDetailsUiModel(
    val formattedAmount: String = "",
    val amountColor: Color = Color.Unspecified,
    val description: String = "",
    val categoryName: String = "",
    val categoryIconIdentifier: String? = null,
    val formattedDate: String = "",
    val accountName: String = "",
    val transactionType: String = "",
    val lineItems: List<TransactionItemUiModel> = emptyList(),
    val receiptImageUrl: String? = null
)

// Model untuk satu baris item di UI
data class TransactionItemUiModel(
    val name: String,
    val quantityAndPrice: String, // Contoh: "(2 x Rp20.000)"
    val formattedTotal: String
)

// UiState utama yang sudah disempurnakan
data class TransactionDetailUiState(
    val isLoading: Boolean = true,
    val details: TransactionDetailsUiModel? = null, // <-- Ganti 'transaction' dengan ini
    val error: String? = null,
    val showDeleteConfirmDialog: Boolean = false
)