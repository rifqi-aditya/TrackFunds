package com.rifqi.trackfunds.core.navigation.graphs

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rifqi.trackfunds.core.navigation.api.AccountSetup
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.feature.auth.screen.AuthScreen
import com.rifqi.trackfunds.feature.onboarding.ui.accountsetup.AccountSetupScreen

/**
 * Composable placeholder untuk layar yang belum diimplementasikan.
 */
@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(name) }
}


/**
 * Komponen NavHost utama yang merakit semua grafik navigasi aplikasi.
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: AppScreen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        modifier = modifier,
    ) {
        composable<Auth> {
            AuthScreen(
                onNavigate = {
                    navController.navigate(HomeGraph) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        composable<AccountSetup> {
            AccountSetupScreen(
                onNavigateHome = {
                    navController.navigate(HomeGraph) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        homeNavGraph(navController)
        transactionsNavGraph(navController)
        budgetNavGraph(navController)
        reportsNavGraph(navController)
        scanNavGraph(navController)
        profileNavGraph(navController)
        accountsNavGraph(navController)
        savingsNavGraph(navController)

        sharedNavGraph(navController)
    }
}