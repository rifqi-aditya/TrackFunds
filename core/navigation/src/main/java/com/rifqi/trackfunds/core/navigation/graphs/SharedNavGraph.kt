package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.rifqi.account.ui.screen.SelectAccountScreen
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.SharedRoutes
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.feature.categories.ui.screen.AddEditCategoryScreen
import com.rifqi.trackfunds.feature.categories.ui.screen.CategoriesScreen
import com.rifqi.trackfunds.feature.categories.ui.screen.SelectCategoryScreen
import com.rifqi.trackfunds.feature.transaction.ui.screen.TransactionDetailScreen

/**
 * Mendefinisikan grafik navigasi untuk rute-rute bersama (shared)
 * yang dapat diakses dari berbagai fitur.
 *
 * @param navController Controller navigasi utama.
 */
fun NavGraphBuilder.sharedNavGraph(navController: NavHostController) {

    composable<SharedRoutes.Categories> {
        CategoriesScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigate = { screen -> navController.navigate(screen) }
        )
    }

    composable<SharedRoutes.AddEditCategory> {
        AddEditCategoryScreen(
            onNavigateBack = { navController.popBackStack() }
        )
    }

    composable<SharedRoutes.SelectCategory> {
        SelectCategoryScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToAddCategory = { navController.navigate(SharedRoutes.AddEditCategory()) },
            onSearchClicked = { },
        )
    }

    composable<SharedRoutes.SelectAccount> {
        SelectAccountScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToAddAccount = { navController.navigate(AccountRoutes.AddEditAccount()) }
        )
    }

    composable<SharedRoutes.Transfer> {
//         TransferScreen(
//             onNavigateBack = { navController.popBackStack() }
//         )
        PlaceholderScreen(name = "Transfer Screen")
    }

    composable<TransactionRoutes.TransactionDetail> {
        TransactionDetailScreen(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToEdit = { transactionId ->
                navController.navigate(TransactionRoutes.AddEditTransaction(transactionId))
            }
        )
    }
}
