package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.account.ui.addedit.AddEditAccountScreen
import com.rifqi.account.ui.list.AccountsScreen
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AccountsGraph

fun NavGraphBuilder.accountsNavGraph(navController: NavHostController) {
    navigation<AccountsGraph>(
        startDestination = AccountRoutes.Accounts,
    ) {
        composable<AccountRoutes.Accounts> {
            AccountsScreen(
                onNavigate = { screen ->
                    navController.navigate(screen)
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
//        composable<Transfer> {
//            TransferScreen(
//                onNavigateBack = { navController.popBackStack() },
//                onNavigateToSelectAccount = { navController.navigate(SelectAccount) }
//            )
//        }
        composable<AccountRoutes.AddEditAccount> {
            AddEditAccountScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}