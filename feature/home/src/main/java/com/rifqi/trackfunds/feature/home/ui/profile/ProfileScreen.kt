package com.rifqi.trackfunds.feature.home.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.ProfileRoutes
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                ProfileSideEffect.NavigateToEditProfile -> {
                    onNavigate(ProfileRoutes.EditProfile)
                }
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
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "My Profile",
                isFullScreen = true,
                onNavigateBack = onNavigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        ProfileHeader(
                            photoUrl = uiState.photoUrl,
                            onChangePhotoClicked = { onEvent(ProfileEvent.EditProfileClicked) }
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                    item {
                        SectionHeader(
                            title = "Account Details",
                            onActionClick = { onEvent(ProfileEvent.EditProfileClicked) }
                        )
                        Spacer(Modifier.height(16.dp))
                    }

                    item { InfoRow(label = "Full Name", value = uiState.userName) }
                    item { InfoRow(label = "Email Address", value = uiState.userEmail) }
                    item {
                        InfoRow(
                            label = "Phone Number",
                            value = uiState.phoneNumber.ifEmpty { "Not set" }
                        )
                    }
                    item {
                        InfoRow(
                            label = "Birthdate",
                            value = uiState.birthdate.ifEmpty { "Not set" }
                        )
                    }

                    item {
                        Spacer(Modifier.height(32.dp))
                        TextButton(onClick = {}) {
                            Text("Hapus Akun", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ProfileHeader(photoUrl: String?, onChangePhotoClicked: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        AsyncImage(
            model = photoUrl,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            placeholder = rememberVectorPainter(image = Icons.Default.Person),
            error = rememberVectorPainter(image = Icons.Default.Person)
        )
        TextButton(onClick = onChangePhotoClicked) {
            Text("Ubah Foto Profil")
        }
    }
}

@Composable
fun SectionHeader(title: String, onActionClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onActionClick) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    leadingIcon: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (leadingIcon != null) {
                leadingIcon()
            }
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
}

@Preview(showBackground = true)
@Composable
private fun ProfileContentPreview() {
    MaterialTheme {
        ProfileContent(
            uiState = ProfileUiState(
                isLoading = false,
                userName = "Rifqi Aditya",
                userEmail = "rifqi@email.com",
                photoUrl = null
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}