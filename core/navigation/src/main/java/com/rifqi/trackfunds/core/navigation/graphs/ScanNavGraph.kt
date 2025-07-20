package com.rifqi.trackfunds.core.navigation.graphs

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.HomeRoutes
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.ScanRoutes
import com.rifqi.trackfunds.core.navigation.api.TransactionRoutes
import com.rifqi.trackfunds.feature.scan.ui.screen.CameraScanScreen
import com.rifqi.trackfunds.feature.scan.ui.screen.ReceiptPreviewScreen
import com.rifqi.trackfunds.feature.scan.ui.screen.ScanOptionScreen

fun NavGraphBuilder.scanNavGraph(navController: NavHostController) {
    navigation<ScanGraph>(
        startDestination = ScanRoutes.ScanOption,
    ) {
        composable<ScanRoutes.ScanOption> {
            ScanOptionScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate(ScanRoutes.CameraScan) },
                onNavigateToPreview = { imageUri ->
                    val encodedUri = Uri.encode(imageUri.toString())
                    navController.navigate(ScanRoutes.ReceiptPreview(imageUri = encodedUri))
                }
            )
        }

        composable<ScanRoutes.CameraScan> {
            CameraScanScreen(
                onNavigateBack = { navController.popBackStack() },
                onPhotoTaken = { uri ->
                    val encodedUri = Uri.encode(uri.toString())

                    navController.navigate(ScanRoutes.ReceiptPreview(imageUri = encodedUri)) {
                        popUpTo<ScanRoutes.ScanOption>()
                    }
                }
            )
        }

        composable<ScanRoutes.ReceiptPreview> {
            ReceiptPreviewScreen(
                onNavigateBack = { navController.popBackStack() },
                onScanSuccessAndNavigate = {
                    navController.popBackStack<HomeRoutes.Home>(inclusive = false)
                    navController.navigate(TransactionRoutes.AddEditTransaction())
                }
            )
        }
    }
}