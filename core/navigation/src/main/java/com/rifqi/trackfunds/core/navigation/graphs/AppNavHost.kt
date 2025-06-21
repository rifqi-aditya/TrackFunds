package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.rifqi.account.ui.screen.AccountTimelineScreen
import com.rifqi.account.ui.screen.AccountsScreen
import com.rifqi.account.ui.screen.SelectAccountScreen
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AccountTimeline
import com.rifqi.trackfunds.core.navigation.api.Accounts
import com.rifqi.trackfunds.core.navigation.api.AccountsGraph
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AddEditTransactionGraph
import com.rifqi.trackfunds.core.navigation.api.AllTransactions
import com.rifqi.trackfunds.core.navigation.api.BalanceDetails
import com.rifqi.trackfunds.core.navigation.api.Budgets
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.Profile
import com.rifqi.trackfunds.core.navigation.api.ProfileGraph
import com.rifqi.trackfunds.core.navigation.api.SelectAccount
import com.rifqi.trackfunds.core.navigation.api.SelectCategory
import com.rifqi.trackfunds.core.navigation.api.Settings
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.core.navigation.api.Transfer
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.home.ui.screen.HomeScreen
import com.rifqi.trackfunds.feature.profile.screen.ProfileScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.AddEditTransactionScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.AllTransactionsScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.CategoryTransactionsScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransferScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TypedTransactionsScreen

// Placeholder untuk layar yang belum dibuat
@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(name) }
}

@Composable
fun AppNavHost(
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
                    onNavigateToAllTransactions = {
                        navController.navigate(AllTransactions)
                    },
                    onNavigateToCategoryTransactions = { categoryId, categoryName ->
                        navController.navigate(CategoryTransactions(categoryId, categoryName))
                    },
                    onNavigateToTypeTransactions = { transactionType ->
                        navController.navigate(
                            when (transactionType) {
                                TransactionType.INCOME -> TypedTransactions(transactionType)
                                TransactionType.EXPENSE -> TypedTransactions(transactionType)
                            }
                        )
                    },
                    onNavigateToNotifications = {
                        navController.navigate(Notifications)
                    },
                    onNavigateToAddTransaction = {
                        navController.navigate(AddEditTransaction())
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
                    onNavigateToTransfer = {
                        navController.navigate(Transfer)
                    }
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
                        navController.navigate(AddEditTransaction())
                    }
                )
            }
            composable<Transfer> {
                TransferScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSelectAccount = { navController.navigate(SelectAccount) }
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

        navigation<AddEditTransactionGraph>(
            startDestination = AddEditTransaction()
        ) {
            composable<AddEditTransaction> {
//                val viewModel: AddEditTransactionViewModel = hiltViewModel(addTransactionGraphEntry)

                AddEditTransactionScreen(
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
                    onNavigateToAddAccount = { /* TODO */ },
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

        composable<AllTransactions> {
            AllTransactionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditTransaction = { transactionId ->
                    navController.navigate(AddEditTransaction(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransactionGraph)
                }
            )
        }

        composable<CategoryTransactions> {
            CategoryTransactionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditTransaction = { transactionId ->
                    navController.navigate(AddEditTransaction(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransactionGraph)
                }
            )
        }

        composable<TypedTransactions> { backStackEntry ->
            TypedTransactionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditTransaction = { transactionId ->
                    navController.navigate(AddEditTransaction(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransactionGraph)
                }
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