package com.rifqi.trackfunds

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rifqi.trackfunds.core.navigation.graphs.AppNavHost
import com.rifqi.trackfunds.core.navigation.ui.components.AppBottomNavigationBar
import com.rifqi.trackfunds.core.navigation.ui.components.bottomNavItemsList
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackFundsTheme {
                TrackFundsMainApp()
            }
        }
    }
}

@Composable
fun TrackFundsMainApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = bottomNavItemsList.any { navItem ->
        currentDestination?.hierarchy?.any { dest ->
            dest.route == navItem.graphRoute::class.qualifiedName
        } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(navController = navController)
            }
        },
        contentWindowInsets = WindowInsets.navigationBars.add(WindowInsets.ime)
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}