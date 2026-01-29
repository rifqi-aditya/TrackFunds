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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.ui.R
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
                is TransactionDetailSideEffect.NavigateToEdit -> onNavigateToEdit(effect.transactionId)
                is TransactionDetailSideEffect.NavigateBackAfterDelete -> onNavigateBack()
                is TransactionDetailSideEffect.ShowSnackbar -> { /* Handle snackbar */
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
            title = { Text(stringResource(R.string.dialog_delete_title)) },
            text = { Text(stringResource(R.string.dialog_delete_message)) },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(TransactionDetailEvent.ConfirmDeleteClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text(stringResource(R.string.button_delete)) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(TransactionDetailEvent.DismissDeleteDialog) }) {
                    Text(stringResource(R.string.button_cancel))
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
                title = stringResource(R.string.transaction_detail_title),
                onNavigateBack = onNavigateBack,
                isFullScreen = true,
                actions = {
                    if (uiState.details != null) {
                        IconButton(onClick = { onEvent(TransactionDetailEvent.EditClicked) }) {
                            Icon(Icons.Rounded.Edit, stringResource(R.string.cd_edit_transaction))
                        }
                        IconButton(onClick = { onEvent(TransactionDetailEvent.DeleteClicked) }) {
                            Icon(
                                Icons.Rounded.Delete,
                                stringResource(R.string.cd_delete_transaction)
                            )
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
                        Text(
                            details.description,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center
                        )
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
                            DetailRow(
                                label = stringResource(R.string.label_type),
                                value = details.transactionType.asString()
                            )
                            DetailRow(
                                label = stringResource(R.string.label_account),
                                value = details.accountName
                            )
                        }
                    }

                    if (details.lineItems.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                stringResource(R.string.label_items),
                                style = MaterialTheme.typography.titleMedium
                            )
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
                                        if (index < details.lineItems.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 8.dp),
                                                thickness = DividerDefaults.Thickness,
                                                color = DividerDefaults.color
                                            )
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
                                stringResource(R.string.label_receipt),
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
                                    contentDescription = stringResource(R.string.cd_receipt_image),
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    loading = { CircularProgressIndicator() },
                                    error = {
                                        Text(
                                            stringResource(R.string.error_load_image),
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.quantityAndPrice,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = item.formattedTotal,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TransactionType.asString(): String {
    return when (this) {
        TransactionType.EXPENSE -> stringResource(id = R.string.transaction_type_expense)
        TransactionType.INCOME -> stringResource(id = R.string.transaction_type_income)
        TransactionType.SAVINGS -> stringResource(id = R.string.transaction_type_savings)
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
                    transactionType = TransactionType.EXPENSE,
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