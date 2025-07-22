package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.ProfileGraph
import com.rifqi.trackfunds.core.navigation.api.ProfileRoutes
import com.rifqi.trackfunds.feature.profile.screen.ProfileScreen

fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {
    navigation<ProfileGraph>(
        startDestination = ProfileRoutes.Profile,
    ) {
        composable<ProfileRoutes.Profile> {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigate = { screen ->
                    navController.navigate(screen)
                },
                onNavigateToLogin = {
                    // Logika logout yang kompleks dan bersih ada di sini
                    navController.navigate(Auth) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}