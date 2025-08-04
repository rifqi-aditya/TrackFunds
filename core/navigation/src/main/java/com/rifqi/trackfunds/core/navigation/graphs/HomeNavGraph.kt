package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.HomeRoutes
import com.rifqi.trackfunds.feature.home.ui.home.HomeScreen

fun NavGraphBuilder.homeNavGraph(navController: NavHostController) {
    navigation<HomeGraph>(
        startDestination = HomeRoutes.Home,
    ) {
        composable<HomeRoutes.Home>(
            enterTransition = { fadeIn(animationSpec = tween(400)) },
            exitTransition = { fadeOut(animationSpec = tween(400)) }
        ) {
            HomeScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigate = { screen ->
                    navController.navigate(screen) {
                        // Pop up ke start destination dari graph untuk
                        // menghindari tumpukan back stack yang besar.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Hindari membuat instance baru dari destinasi yang sama
                        launchSingleTop = true
                        // Kembalikan state saat memilih kembali item yang sama
                        restoreState = true
                    }
                }
            )
        }
    }
}