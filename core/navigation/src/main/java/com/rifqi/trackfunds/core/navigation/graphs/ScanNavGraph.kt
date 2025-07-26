package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.ScanRoutes
import com.rifqi.trackfunds.feature.scan.ui.screen.CameraScanScreen
import com.rifqi.trackfunds.feature.scan.ui.screen.ScanReceiptScreen

const val CAPTURED_IMAGE_URI = "captured_image_uri"

fun NavGraphBuilder.scanNavGraph(navController: NavHostController) {
    navigation<ScanGraph>(
        startDestination = ScanRoutes.ScanReceipt,
    ) {
        composable<ScanRoutes.ScanReceipt> {
            ScanReceiptScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate(ScanRoutes.CameraScan) },
            )
        }

        composable<ScanRoutes.CameraScan> {
            CameraScanScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}