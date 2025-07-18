package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
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
import com.rifqi.trackfunds.core.navigation.api.AddEditCategory
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.Categories
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.Notifications
import com.rifqi.trackfunds.core.navigation.api.Report
import com.rifqi.trackfunds.core.navigation.api.SelectAccount
import com.rifqi.trackfunds.core.navigation.api.SelectCategory
import com.rifqi.trackfunds.core.navigation.api.Settings
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.feature.categories.ui.screen.AddEditCategoryScreen
import com.rifqi.trackfunds.feature.categories.ui.screen.CategoriesScreen
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.AddEditTransactionScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransactionDetailScreen

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
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        modifier = modifier,
    ) {

        homeNavGraph(navController)
        transactionsNavGraph(navController)
        budgetNavGraph(navController)
        reportsNavGraph(navController)
        scanNavGraph(navController)
        profileNavGraph(navController)
        accountsNavGraph(navController)
        savingsNavGraph(navController)


        composable<AddEditTransaction>(
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
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

        composable<Categories> {
            CategoriesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigate = { screen ->
                    navController.navigate(screen)
                }
            )
        }

        composable<AddEditCategory> {
            AddEditCategoryScreen {
                navController.popBackStack()
            }
        }

        // --- Layar Full-Screen ---

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

        composable<Report> {

        }
    }
}