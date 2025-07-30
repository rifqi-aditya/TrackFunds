package com.rifqi.account.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.account.ui.components.AccountCard
import com.rifqi.account.ui.event.AccountsEvent
import com.rifqi.account.ui.preview.AccountListPreviewParameterProvider
import com.rifqi.account.ui.state.AccountsUiState
import com.rifqi.account.ui.viewmodel.AccountsViewModel
import com.rifqi.trackfunds.core.domain.model.Account
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = hiltViewModel(),
    onNavigate: (AppScreen) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { screen ->
            onNavigate(screen)
        }
    }

    AccountsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsContent(
    uiState: AccountsUiState,
    onEvent: (AccountsEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Manage Account", style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    )
                },
                navigationIcon = {
                    DisplayIconFromResource(
                        identifier = "arrow_back",
                        contentDescription = "Accounts Overview",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp)
                            .clickable { onNavigateBack() },
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                windowInsets = TopAppBarDefaults.windowInsets,
                modifier = Modifier.padding(
                    horizontal = 16.dp
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(AccountsEvent.AddAccountClicked) },
                shape = MaterialTheme.shapes.medium,
                containerColor = TrackFundsTheme.extendedColors.accent
            ) {
                DisplayIconFromResource(
                    identifier = "plus",
                    contentDescription = "Add Account",
                    modifier = Modifier
                        .padding(8.dp)
                        .size(24.dp),
                    tint = TrackFundsTheme.extendedColors.onAccent
                )
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding), contentAlignment = Alignment.Center
            ) {
                Text(uiState.error, color = MaterialTheme.colorScheme.error)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Mungkin ada header untuk Total Balance di sini
                // item { TotalBalanceHeader(uiState.totalBalance) }

                items(
                    items = uiState.accounts,
                    key = { it.id }
                ) { account ->
                    AccountCard(
                        account = account,
                        onClick = { onEvent(AccountsEvent.AccountClicked(account.id)) }
                    )
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, name = "Accounts Screen Light")
@Composable
private fun AccountsScreenLightPreview(
    @PreviewParameter(AccountListPreviewParameterProvider::class) accounts: List<Account>
) {
    TrackFundsTheme(darkTheme = false) {
        val totalBalance = accounts.sumOf { it.balance }
        val uiState = AccountsUiState(
            isLoading = false,
            totalBalance = totalBalance,
            accounts = accounts
        )

        AccountsContent(
            uiState = uiState,
            onEvent = { },
            onNavigateBack = { },
        )
    }
}