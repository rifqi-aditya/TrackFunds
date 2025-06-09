package com.rifqi.trackfunds.core.navigation

// Kunci untuk argumen
const val ARG_TRANSACTION_TYPE = "transactionType"
const val ARG_WALLET_ID = "walletId"

// Rute untuk Graph
object NavGraphs {
    const val ROOT = "root_graph" // Graph NavHost utama
    const val MAIN_APP_FLOW =
        "main_app_flow_graph" // Graph setelah login / splash, berisi bottom nav & full screens

    // Rute untuk setiap tab di Bottom Navigation (ini adalah nested graph)
    const val HOME_TAB_GRAPH = "home_tab_graph"
    const val ACCOUNTS_TAB_GRAPH = "accounts_tab_graph"
    const val BUDGETS_TAB_GRAPH = "budgets_tab_graph"
    const val PROFILE_TAB_GRAPH = "profile_tab_graph"
}

// Rute untuk Layar Individual
sealed class Screen(val route: String) {
    // Layar di dalam Home Graph
    object Home : Screen("home_screen")
    object AllTransactions : Screen("all_transactions_screen/{$ARG_TRANSACTION_TYPE}") {
        fun createRoute(transactionType: String) = "all_transactions_screen/$transactionType"
    }
    object BalanceDetails : Screen("balance_details_screen")
    object Notifications : Screen("notifications_screen")

    // Screen in Accounts Graph
    object Accounts : Screen("accounts_screen")
    object WalletDetail : Screen("wallet_detail_screen/{$ARG_WALLET_ID}") {
        fun createRoute(walletId: String) = "wallet_detail_screen/$walletId"
    }

    // Screen in Budgets Graph
    object Budgets : Screen("budgets_screen")

    // Screen in Profile Graph
    object Profile : Screen("profile_screen")
    object Settings : Screen("settings_screen") // Bisa jadi full screen atau nested

    // Full-Screen (di luar Bottom Nav)
    object AddTransaction : Screen("add_transaction_screen")
    object SelectCategory : Screen("select_category_screen/{$ARG_TRANSACTION_TYPE}") {
        fun createRoute(transactionType: String) = "select_category_screen/$transactionType"
    }
    object SelectAccount : Screen("select_account_screen")
}