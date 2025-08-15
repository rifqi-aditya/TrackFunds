package com.rifqi.trackfunds.feature.transaction.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource
import com.rifqi.trackfunds.feature.transaction.ui.components.DetailRow
import com.rifqi.trackfunds.feature.transaction.ui.components.FullscreenReceiptViewer

@SuppressLint("UseKtx")
@Composable
fun TransactionDetailScreen(
    viewModel: TransactionDetailViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var fullscreenUri by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is TransactionDetailSideEffect.NavigateToEdit -> {
                    onNavigateToEdit(effect.transactionId)
                }

                is TransactionDetailSideEffect.NavigateBackAfterDelete -> {
                    onNavigateBack()
                }

                is TransactionDetailSideEffect.ShowSnackbar -> {
                }

                is TransactionDetailSideEffect.ViewReceipt -> fullscreenUri = effect.imageUriString
            }
        }
    }

    if (!fullscreenUri.isNullOrBlank()) {
        FullscreenReceiptViewer(
            uriString = fullscreenUri!!,
            onClose = { fullscreenUri = null },
            onOpenExternal = {
                val uri = Uri.parse(fullscreenUri)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "image/*")
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(intent)
            }
        )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailContent(
    uiState: TransactionDetailUiState,
    onEvent: (TransactionDetailEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Transaction Details",
                onNavigateBack = onNavigateBack,
                isFullScreen = true,
                actions = {
                    // Tombol aksi hanya muncul jika data sudah dimuat
                    if (uiState.details != null) {
                        IconButton(onClick = { onEvent(TransactionDetailEvent.EditClicked) }) {
                            Icon(Icons.Rounded.Edit, "Edit Transaction")
                        }
                        IconButton(onClick = { onEvent(TransactionDetailEvent.DeleteClicked) }) {
                            Icon(Icons.Rounded.Delete, "Delete Transaction")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            } else if (uiState.details != null) {
                val details = uiState.details
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        DisplayIconFromResource(
                            identifier = details.categoryIconIdentifier,
                            contentDescription = details.categoryName,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(details.description, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = details.formattedAmount,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = details.amountColor
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = details.formattedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Bagian Detail Tambahan
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailRow(label = "Type", value = details.transactionType)
                            DetailRow(label = "Account", value = details.accountName)
                            // Anda bisa menambahkan detail lain di sini
                        }
                    }

                    // Bagian Rincian Item (Kondisional)
                    if (details.lineItems.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Items", style = MaterialTheme.typography.titleMedium)
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 8.dp
                                    )
                                ) {
                                    details.lineItems.forEachIndexed { index, item ->
                                        LineItemRow(item = item)

                                        // Kunci #3: Tambahkan pemisah antar item, kecuali untuk yang terakhir
                                        if (index < details.lineItems.lastIndex) {
                                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (!details.receiptImageUri.isNullOrBlank()) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Receipt",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(220.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        onEvent(TransactionDetailEvent.ReceiptClicked(details.receiptImageUri))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                SubcomposeAsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(details.receiptImageUri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Receipt Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    loading = { CircularProgressIndicator() },
                                    error = {
                                        Text(
                                            "Failed to load image",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
fun LineItemRow(item: TransactionItemUiModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically // Jaga agar semua teks sejajar
    ) {
        // Kolom untuk nama dan detail, akan mengisi semua ruang yang tersedia
        Column(
            modifier = Modifier
                .weight(1f) // <-- Kunci #1: Memberi bobot
                .padding(end = 16.dp) // Beri jarak dari total harga
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold, // Beri penekanan pada nama
                maxLines = 1, // <-- Kunci #2: Batasi hanya satu baris
                overflow = TextOverflow.Ellipsis // <-- Tampilkan "..." jika terlalu panjang
            )
            Text(
                text = item.quantityAndPrice,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // Teks untuk total harga
        Text(
            text = item.formattedTotal,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionDetailContentPreview() {
    TrackFundsTheme {
        TransactionDetailContent(
            uiState = TransactionDetailUiState(
                isLoading = false,
                details = TransactionDetailsUiModel(
                    formattedAmount = "- Rp 75.000",
                    amountColor = Color.Red,
                    description = "Kopi dan Roti di Starbucks",
                    categoryName = "Makanan & Minuman",
                    categoryIconIdentifier = "ic_food",
                    formattedDate = "Sabtu, 02 Agustus 2025, 17:05",
                    accountName = "GoPay",
                    transactionType = "Expense",
                    lineItems = listOf(
                        TransactionItemUiModel("Kopi Susu", "(x2 @ Rp20.000)", "Rp40.000"),
                        TransactionItemUiModel("Roti Coklat", "(x1 @ Rp15.000)", "Rp15.000")
                    )
                )
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}