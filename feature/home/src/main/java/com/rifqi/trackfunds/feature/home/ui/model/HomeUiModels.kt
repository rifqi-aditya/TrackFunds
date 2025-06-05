package com.rifqi.trackfunds.feature.home.ui.model

// Model data untuk satu item transaksi di daftar ringkasan Home
data class HomeTransactionItem(
    val id: String,
    val categoryName: String,
    val iconIdentifier: String?, // Identifier untuk dipetakan ke ikon visual
    val amount: Double,
    val type: String // "Expense" atau "Income", bisa juga enum dari domain jika lebih ketat
)

// Model data untuk ringkasan keseluruhan yang ditampilkan di Home
data class HomeSummary(
    val monthlyBalance: Double,
    val totalExpenses: Double,
    val totalIncome: Double,
    val recentExpenses: List<HomeTransactionItem>,
    val recentIncome: List<HomeTransactionItem>
)

// Data class untuk keseluruhan state UI HomeScreen
data class HomeUiState(
    val isLoading: Boolean = true,
    val currentMonthAndYear: String = "", // Contoh: "June 2025"
    val dateRangePeriod: String = "",   // Contoh: "01 - 30 June 2025"
    val summary: HomeSummary? = null,
    val challengeMessage: String? = null, // Pesan tantangan/notifikasi
    val error: String? = null
)