package com.rifqi.trackfunds.feature.transaction.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.ui.components.AmountInputForm
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.FormSelectorCard
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.transaction.ui.components.NotesInputField
import com.rifqi.trackfunds.feature.transaction.ui.event.TransferEvent
import com.rifqi.trackfunds.feature.transaction.ui.state.AccountSelectionMode
import com.rifqi.trackfunds.feature.transaction.ui.state.TransferUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.TransferViewModel
import java.math.BigDecimal

@Composable
fun TransferScreen(
    viewModel: TransferViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSelectAccount: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.accountSelectionMode) {
        if (uiState.accountSelectionMode != AccountSelectionMode.NONE) {
            onNavigateToSelectAccount()
        }
    }

    LaunchedEffect(uiState.isTransferSuccessful) {
        if (uiState.isTransferSuccessful) {
            onNavigateBack()
        }
    }

    TransferContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferContent(
    uiState: TransferUiState,
    onEvent: (TransferEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = { Text("Transfer Between Accounts") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBackIos,
                            "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormSelectorCard(
                label = "From",
                value = uiState.fromAccount?.name ?: "Choose source account",
                leadingIconRes = uiState.fromAccount?.iconIdentifier,
                onClick = { onEvent(TransferEvent.SelectFromAccountClicked) }
            )

            // Panah pemisah
            Icon(
                Icons.Default.SwapVert,
                contentDescription = "Transfer to",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Form Selector untuk Akun Tujuan
            FormSelectorCard(
                label = "To",
                value = uiState.toAccount?.name ?: "Choose destination account",
                leadingIconRes = uiState.toAccount?.iconIdentifier,
                onClick = { onEvent(TransferEvent.SelectToAccountClicked) }
            )

            // Input lainnya
            AmountInputForm(
                value = uiState.amount,
                onValueChange = { onEvent(TransferEvent.AmountChanged(it)) }
            )
            NotesInputField(
                value = uiState.note,
                onValueChange = { onEvent(TransferEvent.NoteChanged(it)) }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onEvent(TransferEvent.PerformTransferClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) CircularProgressIndicator() else Text("Perform Transfer")
            }
        }
    }
}

private val previewFromAccount = AccountItem(
    id = "acc1",
    name = "BCA Mobile",
    iconIdentifier = "ic_bank_bca", // ganti dengan nama ikon Anda
    balance = BigDecimal("5000000")
)

private val previewToAccount = AccountItem(
    id = "acc2",
    name = "GoPay",
    iconIdentifier = "ic_gopay", // ganti dengan nama ikon Anda
    balance = BigDecimal("500000")
)

// --- FUNGSI-FUNGSI PREVIEW ---

@Preview(name = "Transfer Content - Default", showBackground = true)
@Composable
private fun TransferContentDefaultPreview() {
    TrackFundsTheme {
        TransferContent(
            uiState = TransferUiState(),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Transfer Content - Filled State", showBackground = true)
@Preview(
    name = "Transfer Content - Filled (Dark)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TransferContentFilledPreview() {
    TrackFundsTheme {
        TransferContent(
            uiState = TransferUiState(
                fromAccount = previewFromAccount,
                toAccount = previewToAccount,
                amount = "100000",
                note = "Bayar patungan"
            ),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}

@Preview(name = "Transfer Content - Loading", showBackground = true)
@Composable
private fun TransferContentLoadingPreview() {
    TrackFundsTheme {
        TransferContent(
            uiState = TransferUiState(
                fromAccount = previewFromAccount,
                toAccount = previewToAccount,
                amount = "100000",
                isLoading = true // Mensimulasikan kondisi loading
            ),
            onEvent = {},
            onNavigateBack = {},
        )
    }
}