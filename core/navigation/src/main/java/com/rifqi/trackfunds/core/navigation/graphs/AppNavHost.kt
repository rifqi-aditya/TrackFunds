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
import androidx.navigation.toRoute
import com.rifqi.account.ui.screen.SelectAccountScreen
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AllTransactions
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.ScanReceipt
import com.rifqi.trackfunds.core.navigation.api.SelectAccount
import com.rifqi.trackfunds.core.navigation.api.SelectCategory
import com.rifqi.trackfunds.core.navigation.api.Settings
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.scan.ui.screen.ScanScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.AddEditTransactionScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.AllTransactionsScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.CategoryTransactionsScreen
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

        // --- Panggil Semua Graph untuk Bottom Navigation ---
        homeNavGraph(navController)
        accountsNavGraph(navController)
        budgetNavGraph(navController)
        profileNavGraph(navController)


        composable<AddEditTransaction> {
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

        // --- Layar Full-Screen ---

        composable<AllTransactions> {
            AllTransactionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEditTransaction = { transactionId ->
                    navController.navigate(AddEditTransaction(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransaction())
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
                    navController.navigate(AddEditTransaction())
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
                    navController.navigate(AddEditTransaction())
                }
            )
        }

        composable<TransactionDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<TransactionDetail>()
            PlaceholderScreen(name = "Transaction Detail Screen for ID: ${args.transactionId}")
        }

        composable<Settings> {
            PlaceholderScreen(name = "Settings Screen")
        }
        composable<Notifications> {
            PlaceholderScreen(name = "Notifications Screen")
        }

        composable<ScanReceipt> {
            ScanScreen(onNavigateBack = { navController.popBackStack() })
        }
    }
}