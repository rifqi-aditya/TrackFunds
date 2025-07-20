package com.rifqi.trackfunds.feature.scan.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rifqi.trackfunds.core.navigation.api.ScanRoutes
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.scan.ui.event.ScanOptionEvent
import com.rifqi.trackfunds.feature.scan.ui.state.ScanOptionUiState
import com.rifqi.trackfunds.feature.scan.ui.viewmodel.ScanOptionViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ScanOptionScreen(
    viewModel: ScanOptionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToPreview: (Uri) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                onNavigateToPreview(it)
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { screen ->
            if (screen is ScanRoutes.CameraScan) {
                onNavigateToCamera()
            }
        }
    }


    ScanOptionContent(
        uiState = uiState,
        onEvent = { event ->
            // Jika eventnya adalah klik galeri, launcher yang akan bekerja
            if (event is ScanOptionEvent.SelectFromGalleryClicked) {
                galleryLauncher.launch("image/*")
            } else {
                // Jika event lain, teruskan ke ViewModel
                viewModel.onEvent(event)
            }
        },
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanOptionContent(
    uiState: ScanOptionUiState,
    onEvent: (ScanOptionEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Scan Receipt",
                onNavigateBack = onNavigateBack,
                isFullScreen = true
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val lottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.scan_option)
            )

            Spacer(modifier = Modifier.weight(1f))

            LottieAnimation(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(250.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Upload a Receipt", style = MaterialTheme.typography.titleLarge)
            Text(
                "Just upload a clear photo, and we'll read the details for you.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onEvent(ScanOptionEvent.SelectFromGalleryClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text("Choose from Gallery")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = { onEvent(ScanOptionEvent.SelectFromCameraClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Icon(
                    Icons.Default.PhotoCamera,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text("Use Camera")
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview(showBackground = true, name = "Scan Option Screen")
@Composable
private fun ScanOptionContentPreview() {
    TrackFundsTheme {
        ScanOptionContent(
            uiState = ScanOptionUiState(),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Scan Option Screen - Loading")
@Composable
private fun ScanOptionContentLoadingPreview() {
    TrackFundsTheme {
        ScanOptionContent(
            uiState = ScanOptionUiState(isLoading = true),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}