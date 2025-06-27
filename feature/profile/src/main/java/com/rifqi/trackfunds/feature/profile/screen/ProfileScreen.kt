package com.rifqi.trackfunds.feature.profile.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.profile.components.MenuRowItem
import com.rifqi.trackfunds.feature.profile.components.ProfileTopAppBar
import com.rifqi.trackfunds.feature.profile.components.UserInfoSection
import com.rifqi.trackfunds.feature.profile.model.ProfileUiState
import com.rifqi.trackfunds.feature.profile.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit,
    onNavigateToManageAccounts: () -> Unit,
    onNavigateToManageCategories: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileContent(
        uiState = uiState,
        onSettingsClicked = onNavigateToSettings,
        onManageAccountsClicked = onNavigateToManageAccounts,
        onManageCategoriesClicked = onNavigateToManageCategories,
        onLogoutClicked = {
            viewModel.onLogoutClicked()
            onLogout()
        }
    )
}

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onSettingsClicked: () -> Unit,
    onManageAccountsClicked: () -> Unit,
    onManageCategoriesClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    Scaffold(
        topBar = {
            ProfileTopAppBar(onSettingsClick = onSettingsClicked)
        }
    ) { innerPadding ->
        // FIX 1: Column utama ini TIDAK LAGI memiliki verticalScroll.
        // Tugasnya hanya sebagai kerangka untuk menata item secara vertikal.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // -- Bagian yang TIDAK bisa di-scroll --
            Spacer(modifier = Modifier.height(16.dp))
            UserInfoSection(
                userName = uiState.userName,
                userStatus = uiState.userStatus,
                onAvatarClick = { /* TODO */ },
                onCreateAccountClick = { /* TODO */ }
            )
            Spacer(modifier = Modifier.height(24.dp))

            // FIX 2: Area konten sekarang berada di dalam Column yang bisa scroll
            // dan menggunakan .weight(1f) agar fleksibel.
            Column(
                modifier = Modifier
                    .weight(1f) // Ambil semua sisa ruang di tengah
                    .verticalScroll(rememberScrollState())
            ) {
                // Semua konten yang mungkin panjang diletakkan di sini
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Accounts", style = MaterialTheme.typography.titleMedium)
                    MenuRowItem(
                        title = "Manage Accounts",
                        iconIdentifier = "wallet_account",
                        onClick = onManageAccountsClicked
                    )
                    MenuRowItem(
                        title = "Categories",
                        iconIdentifier = "category",
                        onClick = onManageCategoriesClicked
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Transaction", style = MaterialTheme.typography.titleMedium)
                    MenuRowItem(
                        title = "Export CSV",
                        iconIdentifier = "wallet_account",
                        onClick = { /* TODO */ }
                    )
                }
            }

            // -- Bagian bawah yang TIDAK bisa di-scroll (menempel di bawah) --
            OutlinedButton(
                onClick = onLogoutClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = SolidColor(MaterialTheme.colorScheme.error)
                )
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
            onSettingsClicked = {},
            onManageAccountsClicked = {},
            onManageCategoriesClicked = {},
            onLogoutClicked = {}
        )
    }
}