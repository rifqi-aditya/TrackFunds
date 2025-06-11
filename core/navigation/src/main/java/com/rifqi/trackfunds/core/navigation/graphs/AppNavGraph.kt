package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.rifqi.account.ui.screen.AccountsScreen
import com.rifqi.add_transaction.ui.screen.AddTransactionScreen
import com.rifqi.trackfunds.core.navigation.ARG_TRANSACTION_ID
import com.rifqi.trackfunds.core.navigation.ARG_TRANSACTION_TYPE
import com.rifqi.trackfunds.core.navigation.KEY_SELECTED_CATEGORY
import com.rifqi.trackfunds.core.navigation.NavGraphs
import com.rifqi.trackfunds.core.navigation.Screen
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.home.ui.screen.HomeScreen
import com.rifqi.trackfunds.feature.profile.screen.ProfileScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransactionHistoryScreen

// Placeholder untuk layar yang belum dibuat
@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(name) }
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavGraphs.HOME_TAB_GRAPH, // Mulai dari graph tab Home
        modifier = modifier,
        route = NavGraphs.ROOT
    ) {
        // --- Nested Graph untuk Tab Home ---
        navigation(
            startDestination = Screen.Home.route,
            route = NavGraphs.HOME_TAB_GRAPH
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToAllTransactions = { transactionType ->
                        navController.navigate(Screen.AllTransactions.createRoute(transactionType))
                    },
//                    onNavigateToAddTransaction = {
//                        navController.navigate(Screen.AddTransaction.route)
//                    },
                    onNavigateToBalanceDetails = {
                        navController.navigate(
                            Screen.TransactionDetail.createRoute(
                                "ALL"
                            )
                        )
                    },
                    onNavigateToNotifications = { /* TODO: Navigasi ke layar notifikasi */ }
                )
            }
        }

        // --- Nested Graph untuk Tab Accounts ---
        navigation(
            startDestination = Screen.Accounts.route,
            route = NavGraphs.ACCOUNTS_TAB_GRAPH
        ) {
            composable(Screen.Accounts.route) {
                AccountsScreen(
                    onNavigateToWalletDetail = { accountId ->
                        navController.navigate(Screen.AccountTimeline.createRoute(accountId))
                    },
                    onNavigateToTransfer = { /* TODO: Navigasi ke layar transfer */ }
                )
            }
//            composable(
//                route = Screen.AccountTimeline.route,
//                arguments = listOf(navArgument(ARG_WALLET_ID) { type = NavType.StringType })
//            ) {
//                AccountTimelineScreen(
//                    onNavigateBack = { navController.popBackStack() },
//                    onAddTransactionClick = { navController.navigate(Screen.AddTransaction.route) },
//                    onNavigateToTransactionDetail = { transactionId ->
//                        navController.navigate(Screen.TransactionDetail.createRoute(transactionId))
//                    }
//                )
//            }
        }

        // --- Nested Graph untuk Tab Budgets & Profile (dengan placeholder) ---
        navigation(route = NavGraphs.BUDGETS_TAB_GRAPH, startDestination = Screen.Budgets.route) {
            composable(Screen.Budgets.route) { PlaceholderScreen("Budgets Screen") }
        }
        navigation(route = NavGraphs.PROFILE_TAB_GRAPH, startDestination = Screen.Profile.route) {
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToManageAccounts = { navController.navigate(Screen.Accounts.route) }, // Contoh navigasi
                    onNavigateToManageCategories = { /* TODO */ },
                    onLogout = { /* TODO */ }
                )
            }
        }

        // --- Layar Full-Screen (di luar Bottom Nav tabs) ---
        composable(Screen.AddTransaction.route) {
            AddTransactionScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSelectCategory = { transactionType ->
                    navController.navigate(Screen.SelectCategory.createRoute(transactionType))
                },
                onNavigateToSelectAccount = {
                    navController.navigate(Screen.SelectAccount.route)
                },
            )
        }
        composable(
            route = Screen.SelectCategory.route,
            arguments = listOf(navArgument(ARG_TRANSACTION_TYPE) { type = NavType.StringType })
        ) {
            SelectCategoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onCategorySelected = { selectedCategory ->
                    navController.previousBackStackEntry?.savedStateHandle?.set(
                        KEY_SELECTED_CATEGORY, // Gunakan konstanta
                        selectedCategory
                    )
                    navController.popBackStack()
                },
                onNavigateToAddCategory = { /* TODO */ },
                onSearchClicked = { /* TODO */ }
            )
        }
        composable(route = Screen.SelectAccount.route) {
            // Panggil SelectAccountScreen Anda di sini
            PlaceholderScreen("Select Account Screen")
        }
        composable(
            route = Screen.AllTransactions.route,
            arguments = listOf(navArgument(ARG_TRANSACTION_TYPE) { type = NavType.StringType })
        ) {
            TransactionHistoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTransactionDetail = { transactionId ->
                    navController.navigate(Screen.TransactionDetail.createRoute(transactionId))
                },
                onNavigateToAddTransaction = { navController.navigate(Screen.AddTransaction.route) }
            )
        }
        composable(
            route = Screen.TransactionDetail.route,
            arguments = listOf(navArgument(ARG_TRANSACTION_ID) { type = NavType.StringType })
        ) {
            PlaceholderScreen(name = "Transaction Detail Screen")
        }
        composable(Screen.Settings.route) {
            PlaceholderScreen(name = "Settings Screen")
        }
        composable(Screen.Notifications.route) {
            PlaceholderScreen(name = "Notifications Screen")
        }
        composable(Screen.BalanceDetails.route) {
            PlaceholderScreen(name = "Balance Details Screen")
        }
    }
}