package com.rifqi.trackfunds.core.navigation

// --- KUNCI ARGUMEN & HASIL NAVIGASI ---
const val ARG_TRANSACTION_TYPE = "transactionType"
const val ARG_WALLET_ID = "walletId"
const val ARG_TRANSACTION_ID = "transactionId"

const val KEY_SELECTED_CATEGORY = "selected_category_key"
const val KEY_SELECTED_ACCOUNT = "selected_account_key"

// --- RUTE UNTUK GRAF NAVIGASI ---
object NavGraphs {
    const val ROOT = "root_graph" // Rute untuk NavHost utama
    // Rute untuk setiap tab di Bottom Navigation (ini adalah nested graph)
    const val HOME_TAB_GRAPH = "home_tab_graph"
    const val ACCOUNTS_TAB_GRAPH = "accounts_tab_graph"
    const val BUDGETS_TAB_GRAPH = "budgets_tab_graph"
    const val PROFILE_TAB_GRAPH = "profile_tab_graph"
}

// --- RUTE UNTUK LAYAR INDIVIDUAL ---
sealed class Screen(val route: String) {
    // Layar di dalam Home Graph
    object Home : Screen("home_screen")
    object Notifications : Screen("notifications_screen")
    object BalanceDetails : Screen("balance_details_screen")

    // Layar di dalam Accounts Graph
    object Accounts : Screen("accounts_screen")
    object AccountTimeline : Screen("account_timeline_screen/{$ARG_WALLET_ID}") {
        fun createRoute(walletId: String) = "account_timeline_screen/$walletId"
    }

    // Layar di dalam Budgets Graph
    object Budgets : Screen("budgets_screen")

    // Layar di dalam Profile Graph
    object Profile : Screen("profile_screen")
    object Settings : Screen("settings_screen")

    // Layar di luar Bottom Nav (bisa full screen atau bagian dari alur lain)
    object AllTransactions : Screen("all_transactions_screen/{$ARG_TRANSACTION_TYPE}") {
        fun createRoute(transactionType: String) = "all_transactions_screen/$transactionType"
    }
    object TransactionDetail : Screen("transaction_detail_screen/{$ARG_TRANSACTION_ID}") {
        fun createRoute(transactionId: String) = "transaction_detail_screen/$transactionId"
    }
    object AddTransaction : Screen("add_transaction_screen")
    object SelectCategory : Screen("select_category_screen/{$ARG_TRANSACTION_TYPE}") {
        fun createRoute(transactionType: String) = "select_category_screen/$transactionType"
    }
    object SelectAccount : Screen("select_account_screen")
}