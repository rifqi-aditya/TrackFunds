package com.rifqi.trackfunds.feature.savings.ui.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.savings.ui.components.GoalDetailHeader
import com.rifqi.trackfunds.feature.savings.ui.components.StatCard
import com.rifqi.trackfunds.feature.savings.ui.components.TransactionHistoryItem


@Composable
fun SavingsDetailScreen(
    viewModel: GoalDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEditGoal: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is GoalDetailSideEffect.NavigateBack -> onNavigateBack()
                is GoalDetailSideEffect.NavigateToEditGoal -> onNavigateToEditGoal(effect.goalId)
                is GoalDetailSideEffect.ShowSnackbar -> {
                }
            }
        }
    }

    if (uiState.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(GoalDetailEvent.OnDismissDeleteDialog) },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this goal? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(GoalDetailEvent.OnConfirmDelete) }) {
                    Text(
                        "Delete"
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(GoalDetailEvent.OnDismissDeleteDialog) }) {
                    Text(
                        "Cancel"
                    )
                }
            }
        )
    }

    GoalDetailContent(state = uiState, onEvent = viewModel::onEvent)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun GoalDetailContent(
    state: GoalDetailUiState,
    onEvent: (GoalDetailEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Goal Details") },
                navigationIcon = {
                    IconButton(onClick = { onEvent(GoalDetailEvent.OnNavigateBack) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(GoalDetailEvent.OnEditClicked) }) {
                        Icon(Icons.Default.Edit, "Edit Goal")
                    }
                    IconButton(onClick = { onEvent(GoalDetailEvent.OnDeleteClicked) }) {
                        Icon(Icons.Default.Delete, "Delete Goal")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(GoalDetailEvent.OnAddFundsClicked) }) {
                Icon(Icons.Default.Add, "Add Funds")
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(bottom = 80.dp) // Beri ruang untuk FAB
            ) {
                // Header
                item {
                    GoalDetailHeader(
                        name = state.goalName,
                        iconIdentifier = state.iconIdentifier,
                        progress = state.progress,
                        savedAmount = state.savedAmountFormatted,
                        targetAmount = state.targetAmountFormatted
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }

                // Panel Statistik
                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        state.stats.forEach { stat ->
                            StatCard(
                                label = stat.label,
                                value = stat.value,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Judul Riwayat
                stickyHeader {
                    Text(
                        "History",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                // Daftar Transaksi
                items(state.transactions, key = { it.id }) { transaction ->
                    TransactionHistoryItem(
                        transaction = transaction,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GoalDetailContentPreview() {
    val sampleState = GoalDetailUiState(
        isLoading = false,
        goalName = "Trip to Japan",
        iconIdentifier = "travel",
        progress = 0.75f,
        savedAmountFormatted = "Rp22.500.000",
        targetAmountFormatted = "Rp30.000.000",
        stats = listOf(
            StatItem("Avg. Saving/mo", "Rp2.1M"),
            StatItem("Est. Completion", "4 months")
        ),
        transactions = listOf(
            GoalTransactionUiModel(
                "1",
                "Monthly Salary Contribution",
                "25 Jul 2025",
                "+ Rp2.000.000",
                true
            ),
            GoalTransactionUiModel("2", "Bonus", "15 Jul 2025", "+ Rp500.000", true),
            GoalTransactionUiModel("3", "Withdrawal for snacks", "10 Jul 2025", "- Rp50.000", false)
        )
    )
    TrackFundsTheme {
        GoalDetailContent(state = sampleState, onEvent = {})
    }
}