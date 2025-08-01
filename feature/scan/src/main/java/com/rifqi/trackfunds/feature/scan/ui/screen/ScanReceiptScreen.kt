package com.rifqi.trackfunds.feature.scan.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.scan.ui.components.LinearStepIndicator
import com.rifqi.trackfunds.feature.scan.ui.event.ScanReceiptEvent
import com.rifqi.trackfunds.feature.scan.ui.sideeffect.ScanReceiptSideEffect
import com.rifqi.trackfunds.feature.scan.ui.state.ScanPhase
import com.rifqi.trackfunds.feature.scan.ui.state.ScanReceiptUiState
import com.rifqi.trackfunds.feature.scan.ui.viewmodel.ScanReceiptViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanReceiptScreen(
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavigateToAddTransaction: () -> Unit
) {
    val viewModel: ScanReceiptViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Launcher untuk memilih gambar dari galeri
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onEvent(ScanReceiptEvent.ImageSelected(it)) }
        }
    )

    // Menangani side effect dari ViewModel
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ScanReceiptSideEffect.NavigateBack -> onNavigateBack()
                is ScanReceiptSideEffect.NavigateToCamera -> onNavigateToCamera()
                is ScanReceiptSideEffect.LaunchGallery -> galleryLauncher.launch("image/*")
                is ScanReceiptSideEffect.NavigateToAddTransaction -> onNavigateToAddTransaction()
            }
        }
    }

    ScanContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanContent(
    uiState: ScanReceiptUiState,
    onEvent: (ScanReceiptEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = "Scan Receipt",
                // Tombol kembali akan mereset state atau kembali ke halaman sebelumnya
                onNavigateBack = {
                    if (uiState.currentPhase == ScanPhase.UPLOAD) {
                        onNavigateBack()
                    } else {
//                        onEvent(ScanReceiptEvent.ScanAgainClicked)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            // Step indicator
            val steps = listOf("Upload", "Preview", "Processing")
            LinearStepIndicator(
                currentStep = uiState.currentPhase.ordinal,
                steps = steps,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            AnimatedContent(targetState = uiState.currentPhase, label = "PhaseAnimation") { phase ->
                when (phase) {
                    ScanPhase.UPLOAD -> UploadPhase(onEvent)
                    ScanPhase.IMAGE_PREVIEW -> ImagePreviewPhase(
                        imageUri = uiState.imagePreviewUri,
                        errorMessage = uiState.errorMessage,
                        onConfirm = { onEvent(ScanReceiptEvent.ConfirmImage) },
                        onRetake = { onEvent(ScanReceiptEvent.ScanReceiptAgainClicked) }
                    )

                    ScanPhase.PROCESSING -> ProcessingPhase()
                }
            }
        }
    }
}

@Composable
private fun UploadPhase(onEvent: (ScanReceiptEvent) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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
            onClick = { onEvent(ScanReceiptEvent.SelectFromGalleryClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(
                containerColor = TrackFundsTheme.extendedColors.accent,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface
            ),
        ) {
            Icon(Icons.Default.PhotoLibrary, contentDescription = null)
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Choose from Gallery")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { onEvent(ScanReceiptEvent.SelectFromCameraClicked) }, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(
                1.dp, TrackFundsTheme.extendedColors.accent
            )
        ) {
            Icon(
                Icons.Default.PhotoCamera,
                contentDescription = null,
                tint = TrackFundsTheme.extendedColors.accent
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Use Camera", color = TrackFundsTheme.extendedColors.accent)
        }
    }
}

@Composable
fun ImagePreviewPhase(
    imageUri: Uri?,
    errorMessage: String?,
    onConfirm: () -> Unit,
    onRetake: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Is this receipt clear enough?", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(
            "Make sure all details like items, prices, and total are visible.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(16.dp))

        AsyncImage(
            model = imageUri,
            contentDescription = "Receipt Preview",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentScale = ContentScale.Fit
        )

        AnimatedVisibility(visible = errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            OutlinedButton(
                onClick = onRetake,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(
                    1.dp, TrackFundsTheme.extendedColors.accent
                )
            ) {
                Text("Retake", color = TrackFundsTheme.extendedColors.accent)
            }

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TrackFundsTheme.extendedColors.accent,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                )
            ) {
                Text("Yes, Use This Photo")
            }
        }
    }
}

@Composable
private fun ProcessingPhase() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val lottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.scan_loading)
            )

            LottieAnimation(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier.size(250.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Analyzing your receipt, please wait...")
        }
    }
}