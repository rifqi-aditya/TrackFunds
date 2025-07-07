package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.AddEditSavingsGoal
import com.rifqi.trackfunds.core.navigation.api.Savings
import com.rifqi.trackfunds.core.navigation.api.SavingsDetail
import com.rifqi.trackfunds.core.navigation.api.SavingsGraph
import com.rifqi.trackfunds.feature.savings.ui.screen.AddEditSavingsGoalScreen
import com.rifqi.trackfunds.feature.savings.ui.screen.SavingsDetailScreen
import com.rifqi.trackfunds.feature.savings.ui.screen.SavingsScreen

fun NavGraphBuilder.savingsNavGraph(navController: NavHostController) {
    navigation<SavingsGraph>(
        startDestination = Savings,
    ) {
        composable<Savings> {
            SavingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigate = { screen ->
                    navController.navigate(screen)
                }
            )
        }
        composable<AddEditSavingsGoal> {
            AddEditSavingsGoalScreen {
                navController.popBackStack()
            }
        }
        composable<SavingsDetail> {
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