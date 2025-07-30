package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.SavingsGraph
import com.rifqi.trackfunds.core.navigation.api.SavingsRoutes
import com.rifqi.trackfunds.feature.savings.ui.add_edit_goal.AddEditSavingsGoalScreen
import com.rifqi.trackfunds.feature.savings.ui.detail.SavingsDetailScreen
import com.rifqi.trackfunds.feature.savings.ui.goals_list.SavingsGoalScreen

fun NavGraphBuilder.savingsNavGraph(navController: NavHostController) {
    navigation<SavingsGraph>(
        startDestination = SavingsRoutes.Savings,
    ) {
        composable<SavingsRoutes.Savings> {
            SavingsGoalScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToAddGoal = {
                    navController.navigate(SavingsRoutes.AddEditSavingsGoal())
                },
                onNavigateToGoalDetails = { goalId ->
                    navController.navigate(SavingsRoutes.SavingsDetail(goalId))
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
                onNavigateToEditGoal = {
                    navController.navigate(SavingsRoutes.AddEditSavingsGoal(it))
                }
            )
        }
    }
}