package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.AddEditBudget
import com.rifqi.trackfunds.core.navigation.api.Budgets
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.SelectCategory
import com.rifqi.trackfunds.feature.budget.ui.screen.AddEditBudgetScreen
import com.rifqi.trackfunds.feature.budget.ui.screen.BudgetScreen

fun NavGraphBuilder.budgetNavGraph(navController: NavHostController) {
    navigation<BudgetsGraph>(
        startDestination = Budgets
    ) {
        composable<Budgets> {
            BudgetScreen(
                onNavigateToAddEditBudget = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable<AddEditBudget> {
            AddEditBudgetScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSelectCategory = { period ->
                    navController.navigate(SelectCategory(period))
                }
            )
        }
    }
}