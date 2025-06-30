package com.rifqi.trackfunds.core.navigation.graphs

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.CameraScan
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.navigation.api.ReceiptPreview
import com.rifqi.trackfunds.core.navigation.api.ScanGraph
import com.rifqi.trackfunds.core.navigation.api.ScanOption
import com.rifqi.trackfunds.feature.scan.ui.screen.CameraScanScreen
import com.rifqi.trackfunds.feature.scan.ui.screen.ReceiptPreviewScreen
import com.rifqi.trackfunds.feature.scan.ui.screen.ScanOptionScreen

fun NavGraphBuilder.scanNavGraph(navController: NavHostController) {
    navigation<ScanGraph>(
        startDestination = ScanOption,
    ) {
        composable<ScanOption> {
            ScanOptionScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToCamera = { navController.navigate(CameraScan) },
                onNavigateToPreview = { imageUri ->
                    val encodedUri = Uri.encode(imageUri.toString())
                    navController.navigate(ReceiptPreview(imageUri = encodedUri))
                }
            )
        }

        composable<CameraScan> {
            CameraScanScreen(
                onNavigateBack = { navController.popBackStack() },
                onPhotoTaken = { uri ->
                    val encodedUri = Uri.encode(uri.toString())

                    navController.navigate(ReceiptPreview(imageUri = encodedUri)) {
                        popUpTo<ScanOption>()
                    }
                }
            )
        }

        composable<ReceiptPreview> {
            ReceiptPreviewScreen(
                onNavigateBack = { navController.popBackStack() },
                onScanSuccessAndNavigate = {
                    navController.popBackStack<Home>(inclusive = false)
                    navController.navigate(AddEditTransaction())
                }
            )
        }
    }
}