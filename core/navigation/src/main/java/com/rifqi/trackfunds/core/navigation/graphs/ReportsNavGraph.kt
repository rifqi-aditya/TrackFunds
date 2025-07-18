package com.rifqi.trackfunds.core.navigation.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.rifqi.trackfunds.core.navigation.api.Report
import com.rifqi.trackfunds.core.navigation.api.ReportsGraph
import com.rifqi.trackfunds.feature.reports.ui.screen.ReportScreen

fun NavGraphBuilder.reportsNavGraph(navController: NavHostController) {
    navigation<ReportsGraph>(
        startDestination = Report,
    ) {
        composable<Report> {
            ReportScreen()
        }
    }
}