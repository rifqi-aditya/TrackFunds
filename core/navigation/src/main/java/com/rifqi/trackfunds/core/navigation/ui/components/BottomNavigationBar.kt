package com.rifqi.trackfunds.core.navigation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rifqi.trackfunds.core.navigation.NavGraphs

sealed class BottomNavItem(val graphRoute: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(NavGraphs.HOME_TAB_GRAPH, "Home", Icons.Rounded.Home)
    object Accounts :
        BottomNavItem(NavGraphs.ACCOUNTS_TAB_GRAPH, "Accounts", Icons.Rounded.AccountBalanceWallet)

    object Budgets : BottomNavItem(NavGraphs.BUDGETS_TAB_GRAPH, "Budgets", Icons.Rounded.PieChart)
    object Profile : BottomNavItem(NavGraphs.PROFILE_TAB_GRAPH, "Profile", Icons.Rounded.Person)
}

val bottomNavItemsList = listOf(
    BottomNavItem.Home,
    BottomNavItem.Accounts,
    BottomNavItem.Budgets,
    BottomNavItem.Profile
)

@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar {
        bottomNavItemsList.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.graphRoute } == true,
                onClick = {
                    navController.navigate(screen.graphRoute) {
                        // Pop up ke start destination dari graph untuk menghindari tumpukan back stack besar
                        // saat memilih ulang item yang sama atau berpindah tab.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Hindari membuat banyak copy dari destinasi yang sama saat item dipilih ulang
                        launchSingleTop = true
                        // Restore state saat navigasi kembali ke item yang sudah dipilih sebelumnya
                        restoreState = true
                    }
                }
            )
        }
    }
}