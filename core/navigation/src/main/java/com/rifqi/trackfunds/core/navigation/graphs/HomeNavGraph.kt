package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.AllTransactions
import com.rifqi.trackfunds.core.navigation.api.CategoryTransactions
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.SavingsGraph
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.core.navigation.api.TypedTransactions
import com.rifqi.trackfunds.feature.home.ui.screen.HomeScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation<HomeGraph>(
        startDestination = Home,
    ) {
        composable<Home> {
            HomeScreen(
                onNavigateToAllTransactions = { navController.navigate(AllTransactions) },
                onNavigateToCategoryTransactions = { categoryId, categoryName ->
                    navController.navigate(CategoryTransactions(categoryId, categoryName))
                },
                onNavigateToTypedTransactions = { transactionType ->
                    navController.navigate(TypedTransactions(transactionType))
                },
                onNavigateToNotifications = { navController.navigate(SavingsGraph) },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransaction())
                },
                onNavigateToScanReceipt = {
                    navController.navigate(ScanGraph)
                },
                onNavigateToTransactionDetail = { transactionId ->
                    navController.navigate(TransactionDetail(transactionId))
                }
            )
        }
        composable<Notifications> { PlaceholderScreen(name = "Notifications Screen") }
    }
}