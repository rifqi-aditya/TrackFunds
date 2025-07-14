package com.rifqi.trackfunds.core.navigation.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.ReportsGraph
import com.rifqi.trackfunds.core.ui.R

sealed class BottomNavItem(
    val graphRoute: AppScreen,
    val title: String,
    val icon: Int
) {
    data object Home : BottomNavItem(
        graphRoute = HomeGraph,
        title = "Home",
        icon = R.drawable.bottom_nav_home
    )

    data object Transactions : BottomNavItem(
        graphRoute = com.rifqi.trackfunds.core.navigation.api.Transactions,
        title = "Transactions",
        icon = R.drawable.bottom_nav_accounts
    )

    data object Budgets : BottomNavItem(
        graphRoute = BudgetsGraph,
        title = "Budgets",
        icon = R.drawable.bottom_nav_budgets
    )

    data object Reports : BottomNavItem(
        graphRoute = ReportsGraph,
        title = "Reports",
        icon = R.drawable.bottom_nav_report
    )
}

val bottomNavItemsList = listOf(
    BottomNavItem.Home,
    BottomNavItem.Transactions,
    BottomNavItem.Budgets,
    BottomNavItem.Reports
)

@Composable
fun AppBottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.surface
    ) {
        bottomNavItemsList.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = screen.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(screen.title) },
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(route = screen.graphRoute::class)
                } == true,
                onClick = {
                    navController.navigate(screen.graphRoute) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}