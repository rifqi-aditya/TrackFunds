package com.rifqi.trackfunds

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rifqi.trackfunds.core.navigation.graphs.AppNavHost
import com.rifqi.trackfunds.core.navigation.ui.components.AppBottomNavigationBar
import com.rifqi.trackfunds.core.navigation.ui.components.bottomNavItemsList
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TrackFundsMainApp() {
    val trackFundsMainAppViewModel: TrackFundsMainAppViewModel = hiltViewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(trackFundsMainAppViewModel.snackbarManager) {
        trackFundsMainAppViewModel.snackbarManager.messages.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val showBottomBar = bottomNavItemsList.any { navItem ->
        currentDestination?.hierarchy?.any { dest ->
            dest.route == navItem.graphRoute::class.qualifiedName
        } == true
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionColor = MaterialTheme.colorScheme.primary,
                    dismissActionContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    navController = navController,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        contentWindowInsets = WindowInsets.navigationBars
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}