package com.rifqi.trackfunds.feature.savings.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.savings.ui.components.SavingsGoalRow
import com.rifqi.trackfunds.feature.savings.ui.components.TotalSavingsCard
import com.rifqi.trackfunds.feature.savings.ui.event.SavingsEvent
import com.rifqi.trackfunds.feature.savings.ui.state.SavingsUiState
import com.rifqi.trackfunds.feature.savings.ui.viewmodel.SavingsViewModel
import java.math.BigDecimal
import java.time.LocalDateTime

@Composable
fun SavingsScreen(
    viewModel: SavingsViewModel = hiltViewModel(),
    onNavigate: (AppScreen) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            onNavigate(screen)
        }
    }

    SavingsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsContent(
    uiState: SavingsUiState,
    onEvent: (SavingsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Savings Goals",
                onNavigateBack = onNavigateBack,
                isFullScreen = true,
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(SavingsEvent.AddNewGoalClicked) }) {
                Icon(Icons.Default.Add, "Tambah Tujuan")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Panggil TotalSavingsCard
            item {
                TotalSavingsCard(totalAmount = uiState.totalSavings)
            }

            // Panggil SavingsGoalRow untuk setiap item
            items(uiState.savingsGoalItems, key = { it.id }) { goal ->
                SavingsGoalRow(
                    goal = goal,
                    onClick = { onEvent(SavingsEvent.GoalClicked(goal.id)) }
                )
            }
        }
    }
}

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewSavingsGoalItems = listOf(
    SavingsGoalItem(
        id = "1",
        name = "Liburan ke Jepang",
        targetAmount = BigDecimal("20000000"),
        currentAmount = BigDecimal("7500000"),
        targetDate = LocalDateTime.now().plusMonths(12),
        iconIdentifier = "travel",
        isAchieved = false
    ),
    SavingsGoalItem(
        id = "2",
        name = "Dana Darurat",
        targetAmount = BigDecimal("10000000"),
        currentAmount = BigDecimal("9500000"),
        targetDate = null,
        iconIdentifier = "insurance",
        isAchieved = false
    ),
    SavingsGoalItem(
        id = "3",
        name = "DP Rumah",
        targetAmount = BigDecimal("150000000"),
        currentAmount = BigDecimal("165000000"),
        targetDate = LocalDateTime.now().plusYears(3),
        iconIdentifier = "housing",
        isAchieved = true
    )
)

// --- FUNGSI PREVIEW ---

@Preview(name = "Savings Screen - Loaded", showBackground = true)
@Preview(
    name = "Savings Screen - Loaded (Dark)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SavingsContentLoadedPreview() {
    TrackFundsTheme {
        SavingsContent(
            uiState = SavingsUiState(
                isLoading = false,
                totalSavings = BigDecimal("182000000"),
                savingsGoalItems = previewSavingsGoalItems
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(name = "Savings Screen - Empty", showBackground = true)
@Composable
private fun SavingsContentEmptyPreview() {
    TrackFundsTheme {
        SavingsContent(
            uiState = SavingsUiState(
                isLoading = false,
                totalSavings = BigDecimal.ZERO,
                savingsGoalItems = emptyList() // Daftar kosong
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}