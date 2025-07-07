package com.rifqi.trackfunds.feature.savings.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.SavingsGoal
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.R
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
                title = {
                    Text(
                        "Savings Goal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back),
                            contentDescription = "back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
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
            items(uiState.savingsGoals, key = { it.id }) { goal ->
                SavingsGoalRow(
                    goal = goal,
                    onClick = { onEvent(SavingsEvent.GoalClicked(goal.id)) }
                )
            }
        }
    }
}

// --- DUMMY DATA UNTUK PREVIEW ---
private val previewSavingsGoals = listOf(
    SavingsGoal(
        id = "1",
        name = "Liburan ke Jepang",
        targetAmount = BigDecimal("20000000"),
        currentAmount = BigDecimal("7500000"),
        targetDate = LocalDateTime.now().plusMonths(12),
        iconIdentifier = "travel",
        isAchieved = false
    ),
    SavingsGoal(
        id = "2",
        name = "Dana Darurat",
        targetAmount = BigDecimal("10000000"),
        currentAmount = BigDecimal("9500000"),
        targetDate = null,
        iconIdentifier = "insurance",
        isAchieved = false
    ),
    SavingsGoal(
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
                savingsGoals = previewSavingsGoals
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
                savingsGoals = emptyList() // Daftar kosong
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}