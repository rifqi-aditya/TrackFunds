package com.rifqi.trackfunds.core.navigation.api

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen

// --- Graph Routes ---
@Serializable
data object HomeGraph : AppScreen

@Serializable
data object AccountsGraph : AppScreen

@Serializable
data object BudgetsGraph : AppScreen

@Serializable
data object ProfileGraph : AppScreen

@Serializable
data object ScanGraph : AppScreen

@Serializable
data object ReportsGraph : AppScreen

@Serializable
data object TransactionsGraph : AppScreen

@Serializable
data object SavingsGraph : AppScreen

// --- Screen Routes ---
@Serializable
data object Home : AppScreen

@Serializable
data object Accounts : AppScreen

@Serializable
data object Budgets : AppScreen

@Serializable
data object Profile : AppScreen

@Serializable
data object ScanOption : AppScreen

@Serializable
data object Report : AppScreen

@Serializable
data object Transactions : AppScreen

@Serializable
data object FilterTransactions : AppScreen

@Serializable
data object Settings : AppScreen

@Serializable
data object Notifications : AppScreen

@Serializable
data object Transfer : AppScreen

@Serializable
data class AddEditBudget(val period: String, val budgetId: String? = null) : AppScreen

@Serializable
data class AddEditTransaction(val transactionId: String? = null) : AppScreen

// --- Layar Pemilihan (Shared) ---
@Serializable
data object SelectAccount : AppScreen

@Serializable
data class SelectCategory(val transactionType: String) : AppScreen

// --- Layar Daftar Transaksi (Reusable) ---
@Serializable
data class TransactionDetail(val transactionId: String) : AppScreen

@Serializable
data object CameraScan : AppScreen

@Serializable
data class ReceiptPreview(val imageUri: String) : AppScreen

@Serializable
data object Savings : AppScreen

@Serializable
data class AddEditSavingsGoal(val goalId: String? = null) : AppScreen

@Serializable
data class SavingsDetail(val goalId: String) : AppScreen