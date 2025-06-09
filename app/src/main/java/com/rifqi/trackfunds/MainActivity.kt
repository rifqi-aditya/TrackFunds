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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rifqi.trackfunds.core.navigation.Screen
import com.rifqi.trackfunds.core.navigation.graphs.RootNavGraph
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

    val showBottomBarAndFab = bottomNavItemsList.any { navItem ->
        currentDestination?.hierarchy?.any { it.route == navItem.graphRoute } == true
    }

    Scaffold(
        bottomBar = {
            if (showBottomBarAndFab) {
                AppBottomNavigationBar(navController = navController)
            }
        },
        floatingActionButton = {
            if (showBottomBarAndFab) {
                FloatingActionButton(onClick = {
                    navController.navigate(Screen.AddTransaction.route)
                }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Transaction")
                }
            }
        },
        contentWindowInsets = WindowInsets.navigationBars.add(WindowInsets.ime)
    ) { innerPadding ->
        RootNavGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}