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
import com.rifqi.account.ui.screen.SelectAccountScreen
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AllTransactions
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.SelectAccount
import com.rifqi.trackfunds.core.navigation.api.SelectCategory
import com.rifqi.trackfunds.core.navigation.api.Settings
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.AddEditTransactionScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.AllTransactionsScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.CategoryTransactionsScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransactionDetailScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TypedTransactionsScreen

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

        homeNavGraph(navController)
        accountsNavGraph(navController)
        budgetNavGraph(navController)
        profileNavGraph(navController)
        scanNavGraph(navController)

        composable<AddEditTransaction> {
            AddEditTransactionScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigate = { screen -> navController.navigate(screen) },
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
                onNavigateToDetailTransaction = { transactionId ->
                    navController.navigate(TransactionDetail(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransaction())
                }
            )
        }

        composable<CategoryTransactions> {
            CategoryTransactionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetailTransaction = { transactionId ->
                    navController.navigate(TransactionDetail(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransaction())
                }
            )
        }

        composable<TypedTransactions> { backStackEntry ->
            TypedTransactionsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetailTransaction = { transactionId ->
                    navController.navigate(TransactionDetail(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransaction())
                }
            )
        }

        composable<TransactionDetail> {
            TransactionDetailScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { transactionId ->
                    navController.navigate(AddEditTransaction(transactionId = transactionId))
                }
            )
        }

        composable<Settings> {
            PlaceholderScreen(name = "Settings Screen")
        }
        composable<Notifications> {
            PlaceholderScreen(name = "Notifications Screen")
        }
    }
}