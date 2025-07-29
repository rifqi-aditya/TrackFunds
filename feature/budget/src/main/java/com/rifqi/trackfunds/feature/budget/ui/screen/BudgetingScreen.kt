package com.rifqi.trackfunds.feature.budget.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.budget.ui.components.BudgetSummaryDonutChart
import com.rifqi.trackfunds.feature.budget.ui.components.EnhancedBudgetItemRow
import com.rifqi.trackfunds.feature.budget.ui.components.MonthFilterChips
import com.rifqi.trackfunds.feature.budget.ui.model.BudgetCategory
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetingScreen(
    modifier: Modifier = Modifier
) {
    // Dummy data for static preview
    val availableMonths = listOf("Juni 2024", "Juli 2024", "Agustus 2024", "September 2024")
    var selectedMonth by remember { mutableStateOf("Juli 2024") }

    val budgetCategories = listOf(
        BudgetCategory(
            name = "Makanan & Minuman",
            icon = Icons.Default.Fastfood,
            budgetedAmount = 2000000f,
            spentAmount = 1500000f
        ),
        BudgetCategory(
            name = "Transportasi",
            icon = Icons.Default.LocalGasStation,
            budgetedAmount = 800000f,
            spentAmount = 650000f
        ),
        BudgetCategory(
            name = "Shopping",
            icon = Icons.Default.ShoppingCart,
            budgetedAmount = 1200000f,
            spentAmount = 1350000f // Over budget
        ),
        BudgetCategory(
            name = "Hiburan",
            icon = Icons.Default.Movie,
            budgetedAmount = 500000f,
            spentAmount = 300000f
        ),
        BudgetCategory(
            name = "Transportasi Umum",
            icon = Icons.Default.DirectionsCar,
            budgetedAmount = 400000f,
            spentAmount = 380000f // Near limit
        ),
        BudgetCategory(
            name = "Rumah Tangga",
            icon = Icons.Default.Home,
            budgetedAmount = 1000000f,
            spentAmount = 750000f
        )
    )

    // Calculate totals
    val totalBudget = budgetCategories.sumOf { it.budgetedAmount.toDouble() }.toBigDecimal()
    val totalSpent = budgetCategories.sumOf { it.spentAmount.toDouble() }.toBigDecimal()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Budgeting",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month Filter Chips
            item {
                Column {
                    Text(
                        text = "Filter Bulan",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MonthFilterChips(
                        selectedMonth = selectedMonth,
                        availableMonths = availableMonths,
                        onMonthSelected = { selectedMonth = it }
                    )
                }
            }

            // Budget Summary Donut Chart
            item {
                Column {
                    Text(
                        text = "Ringkasan Total",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BudgetSummaryDonutChart(
                        totalBudget = totalBudget,
                        totalSpent = totalSpent,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Budget Categories Header
            item {
                Text(
                    text = "Daftar Budget per Kategori",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Budget Categories List
            items(budgetCategories) { category ->
                EnhancedBudgetItemRow(
                    budgetCategory = category,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BudgetingScreenPreview() {
    TrackFundsTheme {
        BudgetingScreen()
    }
}

@Preview(showBackground = true, name = "Budgeting Screen - Dark Mode")
@Composable
private fun BudgetingScreenDarkPreview() {
    TrackFundsTheme(darkTheme = true) {
        BudgetingScreen()
    }
}