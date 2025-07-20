package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.core.navigation.api.TransactionsGraph
import com.rifqi.trackfunds.feature.transaction.ui.screen.FilterScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransactionScreen

fun NavGraphBuilder.transactionsNavGraph(navController: NavHostController) {
    navigation<TransactionsGraph>(
        startDestination = TransactionRoutes.Transactions,
    ) {
        composable<TransactionRoutes.Transactions>(
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            TransactionScreen(
                onNavigate = { screen -> navController.navigate(screen) },
            )
        }
        composable<TransactionRoutes.FilterTransactions> {
            FilterScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}