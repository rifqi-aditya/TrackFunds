package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.rifqi.account.ui.screen.AccountTimelineScreen
import com.rifqi.account.ui.screen.AccountsScreen
import com.rifqi.trackfunds.core.navigation.api.AccountTimeline
import com.rifqi.trackfunds.core.navigation.api.Accounts
import com.rifqi.trackfunds.core.navigation.api.AccountsGraph
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.SelectAccount
import com.rifqi.trackfunds.core.navigation.api.TransactionDetail
import com.rifqi.trackfunds.core.navigation.api.Transfer
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransferScreen

fun NavGraphBuilder.accountsNavGraph(navController: NavHostController) {
    navigation<AccountsGraph>(
        startDestination = Accounts,
    ) {
        composable<Accounts> {
            AccountsScreen(
                onNavigateToWalletDetail = { accountId ->
                    navController.navigate(AccountTimeline(accountId = accountId))
                },
                onNavigateToTransfer = { navController.navigate(Transfer) }
            )
        }
        composable<AccountTimeline> { backStackEntry ->
            val route = backStackEntry.toRoute<AccountTimeline>()
            AccountTimelineScreen(
                account = route.accountId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTransactionDetail = { transactionId ->
                    navController.navigate(TransactionDetail(transactionId))
                },
                onNavigateToAddTransaction = {
                    navController.navigate(AddEditTransaction())
                }
            )
        }
        composable<Transfer> {
            TransferScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToSelectAccount = { navController.navigate(SelectAccount) }
            )
        }
    }
}