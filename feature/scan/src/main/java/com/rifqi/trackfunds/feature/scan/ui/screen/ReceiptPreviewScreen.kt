package com.rifqi.trackfunds.feature.scan.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.rifqi.trackfunds.feature.scan.ui.components.FullScreenLottieLoading
import com.rifqi.trackfunds.feature.scan.ui.event.ReceiptPreviewEvent
import com.rifqi.trackfunds.feature.scan.ui.state.ReceiptPreviewUiState
import com.rifqi.trackfunds.feature.scan.ui.viewmodel.ReceiptPreviewViewModel

@Composable
fun ReceiptPreviewScreen(
    viewModel: ReceiptPreviewViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onScanSuccessAndNavigate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Efek untuk navigasi kembali setelah proses selesai
    LaunchedEffect(uiState.isScanSuccessful) {
        if (uiState.isScanSuccessful) {
            onScanSuccessAndNavigate()
        }
    }

    ReceiptPreviewContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

// --- Stateless Composable (Presentational UI) ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptPreviewContent(
    uiState: ReceiptPreviewUiState,
    onEvent: (ReceiptPreviewEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Tinjau Struk") },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Rounded.ArrowBack, "Kembali")
                        }
                    }
                )
            },
            bottomBar = {
                Button(
                    onClick = { onEvent(ReceiptPreviewEvent.ConfirmScanClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    enabled = !uiState.isLoading
                ) {
                    Text("Lanjutkan")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                uiState.imageUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Preview Struk",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
        if (uiState.isLoading) {
            FullScreenLottieLoading()
        }
    }

}
