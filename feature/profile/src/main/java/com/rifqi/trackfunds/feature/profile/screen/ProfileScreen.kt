package com.rifqi.trackfunds.feature.profile.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.profile.components.ProfileHeader
import com.rifqi.trackfunds.feature.profile.components.SettingsItem
import com.rifqi.trackfunds.feature.profile.components.SettingsSwitchItem
import com.rifqi.trackfunds.feature.profile.event.ProfileEvent
import com.rifqi.trackfunds.feature.profile.state.ProfileUiState
import com.rifqi.trackfunds.feature.profile.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var darkModeEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { screen ->
            if (screen is Home) {
                onNavigateBack()
            } else {
                onNavigate(screen)
            }
        }
    }

    ProfileContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    onNavigateBack: () -> Unit,
    darkModeEnabled: Boolean = true
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "My Account",
                onNavigateBack = {},
                isFullScreen = true,
                actions = { Spacer(Modifier.width(56.dp)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    ProfileHeader(
                        name = uiState.userName,
                        email = uiState.email
                    )
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    Column(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        SettingsItem(title = "Akun Saya", onClick = {
                            onEvent(ProfileEvent.ManageAccountsClicked)
                        })
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            thickness = 1.dp
                        )
                        SettingsSwitchItem(
                            title = "Dark Mode",
                            checked = darkModeEnabled,
                            onCheckedChange = { }
                        )
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            thickness = 1.dp
                        )
                        SettingsItem(title = "Kelola Kategori", onClick = {
                            onEvent(ProfileEvent.ManageCategoriesClicked)
                        })
                    }
                }
            }

            OutlinedButton(
                onClick = { /* Aksi Logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp
                    ), // Padding atas agar ada jarak
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.error
                ) // Perbaikan kecil untuk border
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.Logout,
                    contentDescription = "Logout",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("Logout")
            }

            Text(
                text = "Version ${uiState.appVersion}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


// --- Preview ---
@Preview(showBackground = true, name = "Profile Screen Full - Light")
@Composable
fun ProfileScreenLightPreview() {
    TrackFundsTheme(darkTheme = false) {
        val dummyState = ProfileUiState(
            isLoading = false,
            userName = "Rifqi Aditya",
            userStatus = "Anonymous",
            appVersion = "1.0.0-preview"
        )
        ProfileContent(
            uiState = dummyState,
            onEvent = {},
            onNavigateBack = {}
        )
    }
}