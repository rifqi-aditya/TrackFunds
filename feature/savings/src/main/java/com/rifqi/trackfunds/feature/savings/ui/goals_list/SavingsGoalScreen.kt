package com.rifqi.trackfunds.feature.savings.ui.goals_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.savings.ui.components.EmptyState
import com.rifqi.trackfunds.feature.savings.ui.components.SavingsGoalCard
import com.rifqi.trackfunds.feature.savings.ui.components.TotalSavingsSummaryCard
import com.rifqi.trackfunds.feature.savings.ui.goals_list.SavingsGoalEvent
import com.rifqi.trackfunds.feature.savings.ui.model.SavingsGoalUiModel
import com.rifqi.trackfunds.feature.savings.ui.goals_list.SavingsGoalSideEffect
import com.rifqi.trackfunds.feature.savings.ui.goals_list.SavingsGoalUiState
import com.rifqi.trackfunds.feature.savings.ui.goals_list.SavingsGoalViewModel


@Composable
fun SavingsGoalScreen(
    viewModel: SavingsGoalViewModel = hiltViewModel(),
    onNavigateToAddGoal: () -> Unit,
    onNavigateToGoalDetails: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SavingsGoalSideEffect.NavigateToAddGoal -> {
                    onNavigateToAddGoal()
                }

                is SavingsGoalSideEffect.NavigateToGoalDetails -> {
                    onNavigateToGoalDetails(effect.goalId)
                }

                is SavingsGoalSideEffect.ShowSnackbar -> {
                }

                SavingsGoalSideEffect.NavigateBack -> {
                    onNavigateBack()
                }
            }
        }
    }

    SavingsGoalContent(
        state = uiState,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsGoalContent(
    state: SavingsGoalUiState,
    onEvent: (SavingsGoalEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            AppTopAppBar(
                title = "Savings Goals",
                onNavigateBack = { onEvent(SavingsGoalEvent.NavigateBackClicked) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(SavingsGoalEvent.AddGoalClicked) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Tujuan Tabungan")
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.savingsGoals.isEmpty()) {
            EmptyState(
                modifier = Modifier.padding(paddingValues),
                onAddGoalClick = { onEvent(SavingsGoalEvent.AddGoalClicked) }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TotalSavingsSummaryCard(
                    totalAmount = state.totalSaved,
                    numberOfGoals = state.savingsGoals.size,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.savingsGoals, key = { it.id }) { goal ->
                        SavingsGoalCard(
                            goal = goal,
                            onClick = { onEvent(SavingsGoalEvent.GoalClicked(goal.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Populated State")
@Composable
private fun SavingsGoalContentPreview_WithData() {
    // Buat data palsu untuk preview
    val sampleGoals = listOf(
        SavingsGoalUiModel(
            "1",
            "DP Rumah",
            0.5f,
            "Rp75.000.000",
            "Rp150.000.000",
            "Rp 75.000.000",
            "housing",
            "29 Jul 2025"

        ),
        SavingsGoalUiModel(
            "2",
            "Liburan",
            0.9f,
            "Rp27.000.000",
            "Rp30.000.000",
            "Rp 3.000.000",
            "travel",
            "29 Jul 2024"
        )
    )
    val sampleState = SavingsGoalUiState(
        savingsGoals = sampleGoals,
        totalSaved = "Rp102.000.000",
        isLoading = false
    )

    TrackFundsTheme { // Ganti dengan tema aplikasi Anda
        SavingsGoalContent(
            state = sampleState,
            onEvent = {}
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
private fun SavingsGoalContentPreview_Empty() {
    val sampleState = SavingsGoalUiState(
        savingsGoals = emptyList(),
        totalSaved = "Rp 0",
        isLoading = false
    )
    TrackFundsTheme { // Ganti dengan tema aplikasi Anda
        SavingsGoalContent(
            state = sampleState,
            onEvent = {}
        )
    }
}