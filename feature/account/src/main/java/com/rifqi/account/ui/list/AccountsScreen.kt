package com.rifqi.account.ui.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.account.ui.list.components.AccountCard
import com.rifqi.account.ui.list.components.TotalBalanceHeader
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is AccountsSideEffect.NavigateToAddAccount -> onNavigate(
                    AccountRoutes.AddEditAccount()
                )

                is AccountsSideEffect.NavigateToEditAccount -> onNavigate(
                    AccountRoutes.AddEditAccount(
                        sideEffect.accountId
                    )
                )

                is AccountsSideEffect.ShowSnackbar -> {
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "My Accounts",
                isFullScreen = true,
                actions = {
                    IconButton(onClick = { viewModel.onEvent(AccountsEvent.AddAccountClicked) }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Account")
                    }
                },
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        AccountsContent(
            modifier = Modifier.padding(innerPadding),
            accounts = uiState.accounts,
            totalBalance = uiState.formattedTotalBalance,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun AccountsContent(
    modifier: Modifier = Modifier,
    accounts: List<AccountItemUiModel>,
    totalBalance: String,
    onEvent: (AccountsEvent) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            TotalBalanceHeader(
                totalBalance = totalBalance,
                accountCount = accounts.size,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "All Accounts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(accounts, key = { it.id }) { account ->
            AccountCard(
                item = account,
                onClick = { onEvent(AccountsEvent.AccountClicked(account.id)) }
            )
        }
    }
}