package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.rifqi.account.ui.screen.AccountTimelineScreen
import com.rifqi.account.ui.screen.AccountsScreen
import com.rifqi.account.ui.screen.SelectAccountScreen
import com.rifqi.add_transaction.ui.screen.AddTransactionScreen
import com.rifqi.add_transaction.ui.viewmodel.AddTransactionViewModel
import com.rifqi.trackfunds.core.navigation.AccountTimeline
import com.rifqi.trackfunds.core.navigation.Accounts
import com.rifqi.trackfunds.core.navigation.AccountsGraph
import com.rifqi.trackfunds.core.navigation.AddTransaction
import com.rifqi.trackfunds.core.navigation.AddTransactionGraph
import com.rifqi.trackfunds.core.navigation.AllTransactions
import com.rifqi.trackfunds.core.navigation.BalanceDetails
import com.rifqi.trackfunds.core.navigation.Budgets
import com.rifqi.trackfunds.core.navigation.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.Home
import com.rifqi.trackfunds.core.navigation.HomeGraph
import com.rifqi.trackfunds.core.navigation.Notifications
import com.rifqi.trackfunds.core.navigation.Profile
import com.rifqi.trackfunds.core.navigation.ProfileGraph
import com.rifqi.trackfunds.core.navigation.SelectAccount
import com.rifqi.trackfunds.core.navigation.SelectCategory
import com.rifqi.trackfunds.core.navigation.Settings
import com.rifqi.trackfunds.core.navigation.TransactionDetail
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
        startDestination = HomeGraph,
        modifier = modifier,
    ) {
        // --- Nested Graph untuk Tab Home ---
        navigation<HomeGraph>(
            startDestination = Home,
        ) {
            composable<Home> {
                HomeScreen(
                    onNavigateToAllTransactions = { transactionType ->
                        navController.navigate(AllTransactions(transactionType))
                    },
                    onNavigateToBalanceDetails = {
                        // FIX: Navigasi ke BalanceDetails, bukan TransactionDetail("ALL")
                        // kecuali jika itu memang tujuannya.
                        navController.navigate(BalanceDetails)
                    },
                    onNavigateToNotifications = {
                        // FIX: Navigasi ke rute objek Notifications
                        navController.navigate(Notifications)
                    },
                    onNavigateToAddTransaction = {
                        navController.navigate(AddTransaction)
                    }
                )
            }
        }

        // --- Nested Graph untuk Tab Accounts ---
        navigation<AccountsGraph>(
            startDestination = Accounts,
        ) {
            composable<Accounts> {
                AccountsScreen(
                    // FIX: Mengisi logika navigasi yang sebelumnya kosong
                    onNavigateToWalletDetail = { account ->
                        navController.navigate(AccountTimeline(account = account))
                    },
                    onNavigateToTransfer = { /* TODO */ }
                )
            }
            composable<AccountTimeline>
            { backStackEntry ->
                val route = backStackEntry.toRoute<AccountTimeline>()

                // FIX: Menambahkan parameter yang dibutuhkan oleh AccountTimelineScreen
                AccountTimelineScreen(
                    account = route.account,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToTransactionDetail = { transactionId ->
                        navController.navigate(TransactionDetail(transactionId))
                    },
                    onNavigateToAddTransaction = {
                        navController.navigate(AddTransaction)
                    }
                )
            }
        }

        // --- Graph lainnya tetap sama dan sudah benar ---
        navigation<BudgetsGraph>(startDestination = Budgets) {
            composable<Budgets> { PlaceholderScreen("Budgets Screen") }
        }
        navigation<ProfileGraph>(startDestination = Profile) {
            composable<Profile> {
                ProfileScreen(
                    onNavigateToSettings = { navController.navigate(Settings) },
                    onNavigateToManageAccounts = { navController.navigate(Accounts) },
                    onNavigateToManageCategories = { /* TODO */ },
                    onLogout = { /* TODO */ }
                )
            }
        }

        navigation<AddTransactionGraph>(
            startDestination = AddTransaction,
        ) {
            composable<AddTransaction> { navBackStackEntry ->
                val addTransactionGraphEntry = remember(navBackStackEntry) {
                    navController.getBackStackEntry<AddTransactionGraph>()
                }
                val viewModel: AddTransactionViewModel = hiltViewModel(addTransactionGraphEntry)

                AddTransactionScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSelectCategory = { transactionType ->
                        navController.navigate(SelectCategory(transactionType))
                    },
                    onNavigateToSelectAccount = {
                        navController.navigate(SelectAccount)
                    },
                )
            }

            composable<SelectAccount> {
                SelectAccountScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddAccount = { /* TODO */ }
                )
            }

            composable<SelectCategory> {
                SelectCategoryScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAddCategory = { },
                    onSearchClicked = { },
                )
            }
        }

        // --- Layar Full-Screen ---

        // FIX: Mengubah composable AllTransactions menjadi type-safe
        composable<AllTransactions> { backStackEntry ->
            val args = backStackEntry.toRoute<AllTransactions>()
            TransactionHistoryScreen(
                transactionType = args.transactionType,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTransactionDetail = { transactionId ->
                    navController.navigate(TransactionDetail(transactionId))
                },
                onNavigateToAddTransaction = { navController.navigate(AddTransaction) }
            )
        }

        // FIX: Mengubah composable TransactionDetail menjadi type-safe
        composable<TransactionDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionDetail>()
            PlaceholderScreen(name = "Transaction Detail Screen for ID: ${args.transactionId}")
        }

        // FIX: Mengubah composable lainnya menjadi type-safe
        composable<Settings> {
            PlaceholderScreen(name = "Settings Screen")
        }
        composable<Notifications> {
            PlaceholderScreen(name = "Notifications Screen")
        }
        composable<BalanceDetails> {
            PlaceholderScreen(name = "Balance Details Screen")
        }
    }
}