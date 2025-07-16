package com.rifqi.trackfunds.core.navigation.model

import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.BudgetsGraph
import com.rifqi.trackfunds.core.navigation.api.HomeGraph
import com.rifqi.trackfunds.core.navigation.api.ReportsGraph
import com.rifqi.trackfunds.core.ui.R

sealed class NavigationItem(
    val graphRoute: AppScreen,
    val title: String,
    val icon: Int
) {
    data object Home : NavigationItem(
        graphRoute = HomeGraph,
        title = "Home",
        icon = R.drawable.bottom_nav_home
    )

    data object Transactions : NavigationItem(
        graphRoute = com.rifqi.trackfunds.core.navigation.api.Transactions,
        title = "Transactions",
        icon = R.drawable.bottom_nav_accounts
    )

    data object Budgets : NavigationItem(
        graphRoute = BudgetsGraph,
        title = "Budgets",
        icon = R.drawable.bottom_nav_budgets
    )

    data object Reports : NavigationItem(
        graphRoute = ReportsGraph,
        title = "Reports",
        icon = R.drawable.bottom_nav_report
    )
}

val navigationItemsLists = listOf(
    NavigationItem.Home,
    NavigationItem.Transactions,
    NavigationItem.Budgets,
    NavigationItem.Reports
)