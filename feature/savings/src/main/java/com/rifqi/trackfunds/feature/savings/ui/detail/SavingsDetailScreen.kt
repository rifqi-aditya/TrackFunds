package com.rifqi.trackfunds.feature.savings.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.components.TransactionRow
import com.rifqi.trackfunds.feature.savings.ui.components.SavingsDetailHeader
import com.rifqi.trackfunds.feature.savings.ui.detail.SavingsDetailUiState
import com.rifqi.trackfunds.feature.savings.ui.detail.SavingsDetailViewModel

@Composable
fun SavingsDetailScreen(
    viewModel: SavingsDetailViewModel = hiltViewModel(),
    onNavigate: (AppScreen) -> Unit,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            // Jika sinyalnya untuk kembali (setelah hapus), panggil onNavigateBack
            if (screen == null) {
                onNavigateBack()
            } else {
                // Untuk sinyal lain seperti ke halaman Add/Edit
                onNavigate(screen)
            }
        }
    }

    if (uiState.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SavingsDetailEvent.DismissDeleteDialog) },
            title = { Text("Hapus Tujuan") },
            text = { Text("Apakah Anda yakin ingin menghapus tujuan tabungan ini? Semua riwayat setoran terkait akan tetap ada.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(SavingsDetailEvent.ConfirmDeleteClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Hapus") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SavingsDetailEvent.DismissDeleteDialog) }) {
                    Text("Batal")
                }
            }
        )
    }

    SavingsDetailContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingsDetailContent(
    uiState: SavingsDetailUiState,
    onEvent: (SavingsDetailEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.goal?.name ?: "Detail Tabungan") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(SavingsDetailEvent.EditGoalClicked) }) {
                        Icon(Icons.Default.Edit, "Edit Tujuan")
                    }
                    IconButton(onClick = { onEvent(SavingsDetailEvent.DeleteGoalClicked) }) {
                        Icon(Icons.Default.Delete, "Hapus Tujuan")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onEvent(SavingsDetailEvent.AddFundsClicked) }) {
                Icon(Icons.Default.Add, "Tambah Setoran")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                uiState.goal?.let { goal ->
                    SavingsDetailHeader(goal = goal)
                }
            }


            item {
                Text(
                    "Transaction History",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Bagian 3: Daftar Riwayat Transaksi
            items(uiState.history, key = { it.id }) { transaction ->
                TransactionRow(item = transaction, onClick = { /* Navigasi ke detail transaksi */ })
            }
        }
    }
}