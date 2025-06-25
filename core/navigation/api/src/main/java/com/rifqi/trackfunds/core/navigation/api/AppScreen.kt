package com.rifqi.trackfunds.core.navigation.api

import com.rifqi.trackfunds.core.domain.model.TransactionType
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
data object Settings : AppScreen

@Serializable
data object Notifications : AppScreen

@Serializable
data object Transfer : AppScreen

@Serializable
data class AccountTimeline(val accountId: String) : AppScreen

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
data object AllTransactions : AppScreen

@Serializable
data class CategoryTransactions(val categoryId: String, val categoryName: String) : AppScreen

@Serializable
data class TypedTransactions(val transactionType: TransactionType) : AppScreen

@Serializable
data class TransactionDetail(val transactionId: String) : AppScreen

@Serializable
data object ScanReceipt : AppScreen
