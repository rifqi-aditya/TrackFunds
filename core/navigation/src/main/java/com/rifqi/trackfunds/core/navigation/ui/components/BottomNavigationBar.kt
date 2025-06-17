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
import com.rifqi.trackfunds.core.navigation.AccountsGraph
import com.rifqi.trackfunds.core.navigation.AppScreen
import com.rifqi.trackfunds.core.navigation.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.HomeGraph
import com.rifqi.trackfunds.core.navigation.ProfileGraph

sealed class BottomNavItem(
    val graphRoute: AppScreen, // Tipe diubah dari String ke AppScreen
    val title: String,
    val icon: ImageVector
) {
    data object Home : BottomNavItem(
        graphRoute = HomeGraph, // Menggunakan objek rute
        title = "Home",
        icon = Icons.Rounded.Home
    )

    data object Accounts : BottomNavItem(
        graphRoute = AccountsGraph, // Menggunakan objek rute
        title = "Accounts",
        icon = Icons.Rounded.AccountBalanceWallet
    )

    data object Budgets : BottomNavItem(
        graphRoute = BudgetsGraph, // Menggunakan objek rute
        title = "Budgets",
        icon = Icons.Rounded.PieChart
    )

    data object Profile : BottomNavItem(
        graphRoute = ProfileGraph, // Menggunakan objek rute
        title = "Profile",
        icon = Icons.Rounded.Person
    )
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
                selected = currentDestination?.hierarchy?.any {
                    it.route == screen.graphRoute::class.qualifiedName
                } == true,
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