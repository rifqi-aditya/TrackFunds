package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.Profile
import com.rifqi.trackfunds.core.navigation.api.ProfileGraph
import com.rifqi.trackfunds.feature.profile.screen.ProfileScreen

fun NavGraphBuilder.profileNavGraph(navController: NavHostController) {
    navigation<ProfileGraph>(
        startDestination = Profile,
    ) {
        composable<Profile> {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigate = { screen ->
                    navController.navigate(screen)
                },
            )
        }
    }
}