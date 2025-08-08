package com.rifqi.trackfunds.feature.home.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.settings.model.AppTheme
import com.rifqi.trackfunds.core.navigation.api.AccountRoutes
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Auth
import com.rifqi.trackfunds.core.navigation.api.ProfileRoutes
import com.rifqi.trackfunds.core.navigation.api.SharedRoutes
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.home.ui.settings.components.SettingsItem
import com.rifqi.trackfunds.feature.home.ui.settings.components.SettingsSectionHeader
import com.rifqi.trackfunds.feature.home.ui.settings.components.ThemePickerDialog
import com.rifqi.trackfunds.feature.home.ui.settings.components.UserProfileHeader


@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SettingsSideEffect.NavigateToManageAccounts -> onNavigate(AccountRoutes.Accounts)
                is SettingsSideEffect.NavigateToManageCategories -> onNavigate(SharedRoutes.Categories)
                is SettingsSideEffect.NavigateToLogin -> {
                    onNavigate(Auth)
                }

                SettingsSideEffect.NavigateToProfile -> {
                    onNavigate(ProfileRoutes.Profile)
                }

                SettingsSideEffect.NavigateToSecurity -> {}
            }
        }
    }

    if (uiState.showLogoutConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(SettingsEvent.DismissLogoutDialog) },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to logout?") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.ConfirmLogoutClicked) }) {
                    Text("Logout", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(SettingsEvent.DismissLogoutDialog) }) {
                    Text("Cancel")
                }
            }
        )
    }

    ThemePickerDialog(
        showDialog = uiState.showThemePickerDialog,
        currentTheme = uiState.appTheme,
        onDismiss = { viewModel.onEvent(SettingsEvent.DismissThemeDialog) },
        onThemeSelected = { theme -> viewModel.onEvent(SettingsEvent.ThemeChanged(theme)) }
    )

    SettingsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        snackbarHostState = snackbarHostState
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    onNavigateBack: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopAppBar(
                title = "Settings",
                onNavigateBack = onNavigateBack,
                isFullScreen = true
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                UserProfileHeader(
                    userName = uiState.userName,
                    userEmail = uiState.userEmail,
                    photoUrl = uiState.userPhotoUrl,
                    onClick = { onEvent(SettingsEvent.ProfileClicked) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item { SettingsSectionHeader(title = "Data Management") }
            item {
                SettingsItem(
                    icon = Icons.Default.Wallet,
                    title = "Manage Accounts",
                    onClick = { onEvent(SettingsEvent.ManageAccountsClicked) })
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Category,
                    title = "Manage Categories",
                    onClick = { onEvent(SettingsEvent.ManageCategoriesClicked) })
            }

            item { SettingsSectionHeader(title = "Preferences & Security") }
            item {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = "Theme",
                    subtitle = uiState.appTheme.name,
                    onClick = { onEvent(SettingsEvent.ThemeItemClicked) })
            }
            item {
                SettingsItem(
                    icon = Icons.Default.Security,
                    title = "Security",
                    subtitle = "Manage PIN or Biometrics",
                    onClick = { onEvent(SettingsEvent.SecurityClicked) })
            }

            item { SettingsSectionHeader(title = "Other") }
            item {
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.HelpOutline,
                    title = "Help Center",
                    onClick = { /* TODO */ })
            }
            item {
                SettingsItem(
                    icon = Icons.Default.StarBorder,
                    title = "Rate This App",
                    onClick = { /* TODO */ })
            }
            item {
                SettingsItem(
                    icon = Icons.AutoMirrored.Filled.Logout,
                    title = "Logout",
                    onClick = { onEvent(SettingsEvent.LogoutClicked) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsContentPreview() {
    val previewUiState = SettingsUiState(
        isLoading = false,
        userName = "Rifqi",
        userEmail = "rifqi@email.com",
        appTheme = AppTheme.DARK,
        appVersion = "1.0.0"
    )

    TrackFundsTheme {
        SettingsContent(
            uiState = previewUiState,
            onEvent = {},
            onNavigateBack = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}