package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.ProfileGraph
import com.rifqi.trackfunds.core.navigation.api.ProfileRoutes
import com.rifqi.trackfunds.feature.home.ui.settings.SettingsScreen
import com.rifqi.trackfunds.feature.home.ui.profile.edit.EditProfileScreen
import com.rifqi.trackfunds.feature.home.ui.profile.ProfileScreen

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
            )
        }
        composable<ProfileRoutes.Settings> {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigate = { screen -> navController.navigate(screen) }
            )
        }
        composable<ProfileRoutes.EditProfile> {
            EditProfileScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}