package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.account.ui.screen.AccountsScreen
import com.rifqi.account.ui.screen.AddEditAccountScreen
import com.rifqi.trackfunds.core.navigation.api.Accounts
import com.rifqi.trackfunds.core.navigation.api.AccountsGraph
import com.rifqi.trackfunds.core.navigation.api.AddEditAccount
import com.rifqi.trackfunds.core.navigation.api.SelectAccount
import com.rifqi.trackfunds.core.navigation.api.Transfer
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransferScreen

fun NavGraphBuilder.accountsNavGraph(navController: NavHostController) {
    navigation<AccountsGraph>(
        startDestination = Accounts,
    ) {
        composable<Accounts> {
            AccountsScreen(
                onNavigate = { screen ->
                    navController.navigate(screen)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<Transfer> {
            TransferScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSelectAccount = { navController.navigate(SelectAccount) }
            )
        }
        composable<AddEditAccount> {
            AddEditAccountScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}