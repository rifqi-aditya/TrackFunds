package com.rifqi.trackfunds.core.navigation.api

import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen

// --- Nested Graph Routes ---
@Serializable
data object HomeGraph : AppScreen

@Serializable
data object AccountsGraph : AppScreen

@Serializable
data object BudgetsGraph : AppScreen

@Serializable
data object ProfileGraph : AppScreen

// --- Rute untuk Layar Individual ---

// Layar di dalam Home Graph
@Serializable
data object Home : AppScreen

@Serializable
data object Notifications : AppScreen

@Serializable
data object BalanceDetails : AppScreen

// Layar di dalam Accounts Graph
@Serializable
data object Accounts : AppScreen

// Menggunakan custom NavType untuk melewatkan seluruh objek
@Serializable
data class AccountTimeline(val account: String) : AppScreen

// Layar di dalam Budgets Graph
@Serializable
data object Budgets : AppScreen

// Layar di dalam Profile Graph
@Serializable
data object Profile : AppScreen

@Serializable
data object Settings : AppScreen

@Serializable
data object AddTransactionGraph : AppScreen

// Layar Full-Screen
@Serializable
data object AllTransactions : AppScreen

@Serializable
data class CategoryTransactions(
    val categoryId: String,
    val categoryName: String
) : AppScreen

@Serializable
data class TypedTransactions(
    val transactionType: TransactionType
) : AppScreen

@Serializable
data class TransactionDetail(val transactionId: String) : AppScreen

@Serializable
data object AddTransaction : AppScreen

@Serializable
data class SelectCategory(val transactionType: String) : AppScreen

@Serializable
data object SelectAccount : AppScreen
