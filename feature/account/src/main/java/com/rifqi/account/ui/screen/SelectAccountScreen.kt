package com.rifqi.account.ui.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.account.ui.viewmodel.SelectAccountViewModel
import com.rifqi.trackfunds.core.ui.model.SelectionItem
import com.rifqi.trackfunds.core.ui.screen.ReusableSelectionScreen
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

@Composable
fun SelectAccountScreen(
    viewModel: SelectAccountViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onAccountSelected: (accountId: String) -> Unit, // Hanya kembalikan ID
    onNavigateToAddAccount: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ReusableSelectionScreen(
        title = "Choose an Account",
        items = uiState.selectionItems,
        isLoading = uiState.isLoading,
        onNavigateBack = onNavigateBack,
        onAddItemClicked = onNavigateToAddAccount,
        onItemSelected = { selectedAccountId ->
            // Saat item dipilih, panggil callback dengan ID yang dipilih
            onAccountSelected(selectedAccountId)
        },
        topBarActions = {
            IconButton(onClick = { /* TODO: Search accounts */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search Account")
            }
        }
    )
}

@Preview(showBackground = true, name = "Select Account Screen Preview")
@Composable
fun SelectAccountScreenPreview() {
    TrackFundsTheme {
        val dummyItems = listOf(
            SelectionItem(
                id = "1",
                name = "Cash Wallet",
                description = "Rp 1.500.000",
                iconIdentifier = "wallet"
            ),
            SelectionItem(
                id = "2",
                name = "Mbanking BCA",
                description = "Rp 12.750.000",
                iconIdentifier = "ic_bank_bca"
            )
        )
        Surface {
            ReusableSelectionScreen(
                title = "Choose an Account",
                items = dummyItems,
                isLoading = false,
                onNavigateBack = {},
                onItemSelected = {},
                onAddItemClicked = {}
            )
        }
    }
}