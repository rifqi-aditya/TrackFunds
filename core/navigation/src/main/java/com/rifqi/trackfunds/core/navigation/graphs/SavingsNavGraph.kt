package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.SavingsGraph
import com.rifqi.trackfunds.core.navigation.api.SavingsRoutes
import com.rifqi.trackfunds.feature.savings.ui.screen.AddEditSavingsGoalScreen
import com.rifqi.trackfunds.feature.savings.ui.screen.SavingsDetailScreen
import com.rifqi.trackfunds.feature.savings.ui.screen.SavingsScreen

fun NavGraphBuilder.savingsNavGraph(navController: NavHostController) {
    navigation<SavingsGraph>(
        startDestination = SavingsRoutes.Savings,
    ) {
        composable<SavingsRoutes.Savings> {
            SavingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigate = { screen ->
                    navController.navigate(screen)
                }
            )
        }
        composable<SavingsRoutes.AddEditSavingsGoal> {
            AddEditSavingsGoalScreen {
                navController.popBackStack()
            }
        }
        composable<SavingsRoutes.SavingsDetail> {
            SavingsDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigate = { screen ->
                    navController.navigate(screen)
                }
            )
        }
    }
}