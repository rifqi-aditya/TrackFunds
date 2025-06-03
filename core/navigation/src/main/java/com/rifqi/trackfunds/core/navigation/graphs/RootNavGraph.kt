package com.rifqi.trackfunds.navigation.graphs

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
import com.rifqi.trackfunds.core.navigation.ARG_TRANSACTION_TYPE
import com.rifqi.trackfunds.core.navigation.NavGraphs
import com.rifqi.trackfunds.core.navigation.Screen
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.home.ui.screen.HomeScreen

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
                    onNavigateToAddTransaction = {
                        navController.navigate(Screen.AddTransaction.route)
                    }
                )
            }
        }

        // Nested Graph untuk Tab Accounts
        navigation(
            startDestination = Screen.Accounts.route,
            route = NavGraphs.ACCOUNTS_TAB_GRAPH
        ) {
            composable(Screen.Accounts.route) { PlaceholderScreen(name = "Accounts Screen") }
            // composable(Screen.WalletDetail.createRoute("someId")) { ... }
        }

        // Nested Graph untuk Tab Budgets
        navigation(
            startDestination = Screen.Budgets.route,
            route = NavGraphs.BUDGETS_TAB_GRAPH
        ) {
            composable(Screen.Budgets.route) { PlaceholderScreen(name = "Budgets Screen") }
        }

        // Nested Graph untuk Tab Profile
        navigation(
            startDestination = Screen.Profile.route,
            route = NavGraphs.PROFILE_TAB_GRAPH
        ) {
            composable(Screen.Profile.route) { PlaceholderScreen(name = "Profile Screen") }
            // composable(Screen.Settings.route) { SettingsScreen(...) } // Jika settings bagian dari profile tab
        }

        // Layar Full-Screen (di luar Bottom Nav tabs, tapi bagian dari main_app_flow)
        composable(Screen.AddTransaction.route) {
            PlaceholderScreen(name = "Add Transaction Screen")
            // AddTransactionScreen(navController = navController, ...)
        }
        composable(
            route = Screen.SelectCategory.route, // contoh: "select_category_screen/{transactionType}"
            arguments = listOf(navArgument(ARG_TRANSACTION_TYPE) { type = NavType.StringType })
        ) {
            SelectCategoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onCategorySelected = { navController.popBackStack() },
                onAddCategoryClicked = { },
                onSearchActionClicked = { }
            )
        }
    }
}