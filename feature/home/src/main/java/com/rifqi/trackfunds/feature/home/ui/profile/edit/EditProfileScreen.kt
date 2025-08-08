package com.rifqi.trackfunds.feature.home.ui.profile.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.CustomDatePickerDialog
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerMode
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: EditProfileViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Launcher untuk memilih gambar dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onEvent(EditProfileEvent.ImageSelected(it)) }
        }
    )

    // Menangani side effect dari ViewModel
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is EditProfileSideEffect.NavigateBack -> onNavigateBack()
                is EditProfileSideEffect.LaunchImagePicker -> galleryLauncher.launch("image/*")
                is EditProfileSideEffect.ShowSnackbar -> {

                }
            }
        }
    }

    EditProfileContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
    )
}

// --- 2. Stateless Composable (Presentational) ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    uiState: EditProfileUiState,
    onEvent: (EditProfileEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    if (uiState.showDatePicker) {
        CustomDatePickerDialog(
            showDialog = true,
            initialDate = uiState.birthdate ?: LocalDate.now(),
            onDismiss = { onEvent(EditProfileEvent.DismissDatePicker) },
            onConfirm = { newDate -> onEvent(EditProfileEvent.DateSelected(newDate)) }
        )
    }

    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Edit Profile",
                onNavigateBack = onNavigateBack,
                isFullScreen = true
            )
        },
        bottomBar = {
            Button(
                onClick = { onEvent(EditProfileEvent.SaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TrackFundsTheme.extendedColors.accent,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = TrackFundsTheme.extendedColors.accent
                    )
                } else {
                    Text(
                        "Save Changes",
                        color = TrackFundsTheme.extendedColors.onAccent
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ProfileImageEditor(
                        currentImageUrl = uiState.photoUrl,
                        newImageUri = uiState.newImageUri,
                        onChangePhotoClicked = { onEvent(EditProfileEvent.ChangePhotoClicked) }
                    )

                    GeneralTextInputField(
                        value = uiState.fullName,
                        onValueChange = { onEvent(EditProfileEvent.FullNameChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Full Name",
                        isError = uiState.fullNameError != null,
                        errorMessage = uiState.fullNameError,
                    )

                    GeneralTextInputField(
                        value = uiState.phoneNumber,
                        onValueChange = { onEvent(EditProfileEvent.PhoneNumberChanged(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        label = "Phone Number",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    DatePickerField(
                        label = "Birthdate",
                        value = uiState.birthdate,
                        onClick = { onEvent(EditProfileEvent.BirthdateClicked) },
                        mode = DatePickerMode.FULL_DATE
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileImageEditor(
    currentImageUrl: String?,
    newImageUri: Uri?,
    onChangePhotoClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val imageModel = newImageUri ?: currentImageUrl

        // 1. Gunakan Box sebagai bingkai utama
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            // 2. Ikon placeholder di lapisan bawah dengan ukuran proporsional
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Placeholder",
                modifier = Modifier.size(72.dp), // Ukuran ikon yang lebih besar
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // 3. AsyncImage akan menimpa ikon ini saat berhasil dimuat
            AsyncImage(
                model = imageModel,
                contentDescription = "Profile Picture",
                modifier = Modifier.fillMaxSize(), // Penuhi ukuran Box
                contentScale = ContentScale.Crop
            )
        }

        TextButton(onClick = onChangePhotoClicked) {
            Icon(
                Icons.Default.CameraAlt,
                contentDescription = null,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Change Photo")
        }
    }
}

// --- Preview ---

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    MaterialTheme {
        EditProfileScreen(onNavigateBack = {})
    }
}