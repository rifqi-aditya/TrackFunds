package com.rifqi.trackfunds.feature.transaction.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AddEditTransaction
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.theme.extendedColors
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.transaction.ui.components.DetailRow
import com.rifqi.trackfunds.feature.transaction.ui.event.TransactionDetailEvent
import com.rifqi.trackfunds.feature.transaction.ui.state.TransactionDetailUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.TransactionDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { screen ->
            when (screen) {
                is AddEditTransaction -> onNavigateToEdit(screen.transactionId!!)
                is Home -> onNavigateBack() // Kembali setelah berhasil hapus
                else -> {}
            }
        }
    }

    if (uiState.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(TransactionDetailEvent.DismissDeleteDialog) },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(TransactionDetailEvent.ConfirmDeleteClicked) },
                    // Beri warna error pada tombol konfirmasi hapus
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(TransactionDetailEvent.DismissDeleteDialog) }) {
                    Text("Cancel")
                }
            }
        )
    }

    TransactionDetailContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

// --- Stateless Composable (Presentational UI) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailContent(
    uiState: TransactionDetailUiState,
    onEvent: (TransactionDetailEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Transaksi") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBackIos,
                            "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onEvent(TransactionDetailEvent.DeleteClicked) }) {
                        DisplayIconFromResource(
                            identifier = "delete",
                            contentDescription = "Delete Transaction",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = { onEvent(TransactionDetailEvent.EditClicked) }) {
                        DisplayIconFromResource(
                            identifier = "edit",
                            contentDescription = "Edit Transaction",
                            modifier = Modifier.size(
                                24.dp
                            )
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        val transaction = uiState.transaction

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (transaction != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    DisplayIconFromResource(
                        identifier = transaction.categoryIconIdentifier,
                        contentDescription = transaction.categoryName,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f)) // Latar belakang ikon lebih lembut
                            .padding(10.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = transaction.note.ifBlank {
                            "-"
                        },
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = if (transaction.type == TransactionType.INCOME) {
                            "+ ${formatCurrency(transaction.amount)}"
                        } else {
                            "- ${formatCurrency(transaction.amount)}"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (transaction.type == TransactionType.INCOME) {
                            MaterialTheme.extendedColors.income
                        } else {
                            MaterialTheme.extendedColors.expense
                        }
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // --- Bagian Detail (Menggunakan DetailRow) ---
                    DetailRow(label = "Account Source") {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            DisplayIconFromResource(
                                identifier = transaction.categoryIconIdentifier,
                                contentDescription = transaction.accountName,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Color.Gray.copy(
                                            alpha = 0.1f
                                        )
                                    )
                                    .padding(4.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = transaction.accountName,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    DetailRow(label = "Transaction Date") {
                        Text(
                            text = transaction.date.format(
                                DateTimeFormatter.ofPattern(
                                    "dd MMMM yyyy",
                                    Locale.getDefault()
                                )
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    DetailRow(label = "Category") {
                        Text(
                            text = transaction.categoryName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    DetailRow(label = "Type") {
                        Text(
                            text = transaction.type.name.lowercase(Locale.getDefault())
                                .replaceFirstChar { it.uppercase() },
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                }
            }
        }
    }
}


// --- DUMMY DATA UNTUK PREVIEW ---
private val previewTransactionDetail = TransactionItem(
    id = "trx1",
    note = "Beli Bensin di Pertamina",
    amount = BigDecimal("400000"),
    type = TransactionType.EXPENSE,
    date = LocalDateTime.of(2025, 6, 27, 10, 30),
    categoryId = "cat-fuel",
    categoryName = "Bensin",
    categoryIconIdentifier = "local_gas_station",
    accountId = "acc-kas",
    accountName = "KAS",
)

@Preview(showBackground = true, name = "Transaction Detail - Light Mode")
@Preview(
    showBackground = true,
    name = "Transaction Detail - Dark Mode",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TransactionDetailContentPreview() {
    TrackFundsTheme {
        TransactionDetailContent(
            uiState = TransactionDetailUiState(
                isLoading = false,
                transaction = previewTransactionDetail
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}