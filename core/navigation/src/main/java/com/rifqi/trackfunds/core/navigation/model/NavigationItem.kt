package com.rifqi.trackfunds.core.navigation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.outlined.ReceiptLong
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.ui.graphics.vector.ImageVector
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.ReportsGraph

sealed class NavigationItem(
    val graphRoute: AppScreen,
    val title: String,
    val selectedIcon: ImageVector, // <-- Menggunakan ImageVector
    val unselectedIcon: ImageVector // <-- Ikon untuk state tidak terpilih
) {
    data object Home : NavigationItem(
        graphRoute = HomeGraph,
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    data object Transactions : NavigationItem(
        graphRoute = com.rifqi.trackfunds.core.navigation.api.Transactions,
        title = "Transactions",
        selectedIcon = Icons.AutoMirrored.Filled.ReceiptLong,
        unselectedIcon = Icons.AutoMirrored.Outlined.ReceiptLong
    )

    data object Budgets : NavigationItem(
        graphRoute = BudgetsGraph,
        title = "Budgets",
        selectedIcon = Icons.Filled.AccountBalanceWallet,
        unselectedIcon = Icons.Outlined.AccountBalanceWallet
    )

    data object Reports : NavigationItem(
        graphRoute = ReportsGraph,
        title = "Reports",
        selectedIcon = Icons.Filled.PieChart,
        unselectedIcon = Icons.Outlined.PieChart
    )
}

val navigationItemsLists = listOf(
    NavigationItem.Home,
    NavigationItem.Transactions,
    NavigationItem.Budgets,
    NavigationItem.Reports
)