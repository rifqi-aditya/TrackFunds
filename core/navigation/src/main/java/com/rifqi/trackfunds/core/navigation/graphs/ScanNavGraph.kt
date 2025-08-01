package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.ScanRoutes
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.feature.scan.ui.screen.CameraScanScreen
import com.rifqi.trackfunds.feature.scan.ui.screen.ScanReceiptScreen

fun NavGraphBuilder.scanNavGraph(navController: NavHostController) {
    navigation<ScanGraph>(
        startDestination = ScanRoutes.ScanReceipt,
    ) {
        composable<ScanRoutes.ScanReceipt> {
            ScanReceiptScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate(ScanRoutes.CameraScan) },
                onNavigateToAddTransaction = {
                    navController.navigate(TransactionRoutes.AddEditTransaction()) {
                        popUpTo<ScanRoutes.ScanReceipt> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<ScanRoutes.CameraScan> {
            CameraScanScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}