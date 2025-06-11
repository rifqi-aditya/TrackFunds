package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rifqi.account.ui.screen.AccountsScreen
import com.rifqi.account.ui.screen.SelectAccountScreen
import com.rifqi.add_transaction.ui.screen.AddTransactionScreen
import com.rifqi.trackfunds.core.navigation.ARG_TRANSACTION_TYPE
import com.rifqi.trackfunds.core.navigation.NavGraphs
import com.rifqi.trackfunds.core.navigation.Screen
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.home.ui.screen.HomeScreen
import com.rifqi.trackfunds.feature.profile.screen.ProfileScreen

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(name) }
}

@Composable
fun RootNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier // Untuk innerPadding dari Scaffold utama
) {
    NavHost(
        navController = navController,
        startDestination = NavGraphs.MAIN_APP_FLOW, // Mulai dari graph utama aplikasi
        route = NavGraphs.ROOT, // Rute untuk NavHost ini (opsional tapi berguna)
        modifier = modifier // Terapkan padding dari Scaffold utama di sini
    ) {
        // Definisikan mainAppFlowGraph sebagai nested graph di dalam NavHost utama
        // Ini memungkinkan Anda memiliki graph lain di level yang sama jika perlu (misalnya, authGraph)
        mainAppFlowGraph(navController)
    }
}

// Extension function untuk NavGraphBuilder
fun NavGraphBuilder.mainAppFlowGraph(navController: NavHostController) {
    navigation(
        route = NavGraphs.MAIN_APP_FLOW, // Rute untuk graph utama setelah login/splash
        startDestination = NavGraphs.HOME_TAB_GRAPH // Tab default saat graph ini dimulai
    ) {
        // Nested Graph untuk Tab Home
        navigation(
            startDestination = Screen.Home.route,
            route = NavGraphs.HOME_TAB_GRAPH
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToAllTransactions = { transactionType ->
                        navController.navigate(Screen.AllTransactions.createRoute(transactionType))
                    },
                    onNavigateToBalanceDetails = {
                        navController.navigate(Screen.BalanceDetails.route)
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Screen.Notifications.route)
                    }
                )
            }
        }

        // Nested Graph untuk Tab Accounts
        navigation(
            startDestination = Screen.Accounts.route,
            route = NavGraphs.ACCOUNTS_TAB_GRAPH
        ) {
            composable(Screen.Accounts.route) {
                AccountsScreen(
                    onNavigateToTransfer = {},
                    onNavigateToWalletDetail = {},
                )
            }
        }

        // Nested Graph untuk Tab Budgets
        navigation(
            startDestination = Screen.Budgets.route,
            route = NavGraphs.BUDGETS_TAB_GRAPH
        ) {
            composable(Screen.Budgets.route) { PlaceholderScreen(name = "On Development...") }
        }

        // Nested Graph untuk Tab Profile
        navigation(
            startDestination = Screen.Profile.route,
            route = NavGraphs.PROFILE_TAB_GRAPH
        ) {
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = {
                        navController.navigate(Screen.Settings.route)
                    },
                    onNavigateToManageAccounts = { },
                    onNavigateToManageCategories = { },
                    onLogout = { },
                )
            }
        }

        // Layar Full-Screen (di luar Bottom Nav tabs, tapi bagian dari main_app_flow)
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToSelectCategory = { transactionType ->
                    navController.navigate(Screen.SelectCategory.createRoute(transactionType))
                },
                onNavigateToSelectAccount = {
                    navController.navigate(Screen.SelectAccount.route)
                },
            )
        }
        composable(
            route = Screen.SelectCategory.route, // contoh: "select_category_screen/{transactionType}"
            arguments = listOf(navArgument(ARG_TRANSACTION_TYPE) { type = NavType.StringType })
        ) {
            SelectCategoryScreen(
                // Atau SimpleSelectCategoryScreen Anda
                onNavigateBack = { navController.popBackStack() },
                onCategorySelected = { selectedCategory ->
                    // Set hasil untuk AddTransactionScreen
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "selected_category_key",
                        selectedCategory
                    )
                    navController.popBackStack() // Kembali ke AddTransactionScreen
                },
                onNavigateToAddCategory = { TODO() },
                onSearchClicked = { TODO() },
            )
        }
        composable(
            route = Screen.SelectAccount.route
        ) {
            SelectAccountScreen(
                onNavigateBack = { navController.popBackStack() },
                onAccountSelected = { selectedAccountId ->
                    // Set hasil untuk AddTransactionScreen
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        "selected_account_key",
                        selectedAccountId
                    )
                    navController.popBackStack() // Kembali ke AddTransactionScreen
                },
                onNavigateToAddAccount = { TODO() }
            )
        }
    }
}