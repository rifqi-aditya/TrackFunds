package com.rifqi.trackfunds.feature.scan.ui.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.rifqi.trackfunds.core.domain.model.ReceiptItemModel
import com.rifqi.trackfunds.core.ui.R
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.CustomDatePickerDialog
import com.rifqi.trackfunds.core.ui.components.SelectionItem
import com.rifqi.trackfunds.core.ui.components.SelectionList
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerMode
import com.rifqi.trackfunds.core.ui.components.inputfield.FormSelectorField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.scan.ui.components.LinearStepIndicator
import com.rifqi.trackfunds.feature.scan.ui.event.ScanReceiptEvent
import com.rifqi.trackfunds.feature.scan.ui.sideeffect.ScanReceiptSideEffect
import com.rifqi.trackfunds.feature.scan.ui.state.ScanPhase
import com.rifqi.trackfunds.feature.scan.ui.state.ScanReceiptUiState
import com.rifqi.trackfunds.feature.scan.ui.state.ScanSheetType
import com.rifqi.trackfunds.feature.scan.ui.viewmodel.ScanReceiptViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanReceiptScreen(
    // Callback navigasi yang dikelola oleh NavHost
    onNavigateBack: () -> Unit,
    onNavigateToCamera: () -> Unit,
//    onNavigateToConfirm: (ScanResult) -> Unit
) {
    val viewModel: ScanReceiptViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val categoriesForSelection =
        viewModel.categoriesForSelection.collectAsStateWithLifecycle().value

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
            }
        }
    }

    CustomDatePickerDialog(
        showDialog = uiState.showDatePicker,
        initialDate = uiState.editableTransaction?.date?.toLocalDate() ?: LocalDate.now(),
        onDismiss = {
            viewModel.onEvent(ScanReceiptEvent.DismissAllPickers)
        },
        onConfirm = { newDate ->
            viewModel.onEvent(ScanReceiptEvent.DateChanged(newDate))
        }
    )

    if (uiState.activeSheet != null) {
        ModalBottomSheet(onDismissRequest = { viewModel.onEvent(ScanReceiptEvent.DismissAllPickers) }) {
            when (uiState.activeSheet) {
                ScanSheetType.CATEGORY -> {
                    SelectionList(
                        title = "Pilih Kategori",
                        items = categoriesForSelection,
                        itemBuilder = { category ->
                            SelectionItem(category.id, category.name, category.iconIdentifier)
                        },
                        onItemSelected = { category ->
                            viewModel.onEvent(ScanReceiptEvent.CategorySelected(category))
                        },
                        isSearchable = true,
                        searchQuery = uiState.categorySearchQuery,
                        onSearchQueryChanged = { query ->
                            viewModel.onEvent(ScanReceiptEvent.CategorySearchQueryChanged(query))
                        },
                    )
                }

                ScanSheetType.ACCOUNT -> {
                    SelectionList(
                        title = "Select Account",
                        items = uiState.allAccounts,
                        itemBuilder = { account ->
                            SelectionItem(account.id, account.name, account.iconIdentifier)
                        },
                        onItemSelected = { account ->
                            viewModel.onEvent(ScanReceiptEvent.AccountSelected(account))
                        },
                    )
                }

                null -> TODO()
            }
        }
    }

    // Panggil UI stateless
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
            val steps = listOf("Upload", "Preview", "Processing", "Review")
            LinearStepIndicator(
                currentStep = uiState.currentPhase.ordinal,
                steps = steps,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // Konten dinamis berdasarkan fase
            AnimatedContent(targetState = uiState.currentPhase, label = "PhaseAnimation") { phase ->
                when (phase) {
                    ScanPhase.UPLOAD -> UploadPhase(onEvent)
                    ScanPhase.IMAGE_PREVIEW -> ImagePreviewPhase(
                        imageUri = uiState.imagePreviewUri,
                        onConfirm = { onEvent(ScanReceiptEvent.ConfirmImage) },
                        onRetake = { onEvent(ScanReceiptEvent.ScanReceiptAgainClicked) }
                    )

                    ScanPhase.PROCESSING -> ProcessingPhase()
                    ScanPhase.REVIEW -> {
                        // Di sini kita akan menampilkan AddEditTransactionScreen
                        // dengan data yang sudah terisi dari hasil scan.
                        // Untuk saat ini, kita gunakan placeholder ReviewPhase.
                        ReviewPhase(
                            uiState = uiState,
                            onEvent = onEvent
                        )
                    }
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
                containerColor = TrackFundsTheme.extendedColors.accentGreen,
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
                1.dp, TrackFundsTheme.extendedColors.accentGreen
            )
        ) {
            Icon(
                Icons.Default.PhotoCamera,
                contentDescription = null,
                tint = TrackFundsTheme.extendedColors.accentGreen
            )
            Spacer(Modifier.width(ButtonDefaults.IconSpacing))
            Text("Use Camera", color = TrackFundsTheme.extendedColors.accentGreen)
        }
    }
}

@Composable
fun ImagePreviewPhase(
    imageUri: Uri?,
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
                    1.dp, TrackFundsTheme.extendedColors.accentGreen
                )
            ) {
                Text("Retake", color = TrackFundsTheme.extendedColors.accentGreen)
            }

            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TrackFundsTheme.extendedColors.accentGreen,
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

@Composable
private fun ReviewPhase(
    uiState: ScanReceiptUiState,
    onEvent: (ScanReceiptEvent) -> Unit
) {
    val transaction =
        uiState.editableTransaction ?: return

    Scaffold(
        bottomBar = {
            Button(
                onClick = { onEvent(ScanReceiptEvent.SaveTransactionClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text("Save Transaction")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- FORM UTAMA ---
            item {
                GeneralTextInputField(
                    label = "Description (Merchant)",
                    value = transaction.description,
                    onValueChange = { onEvent(ScanReceiptEvent.DescriptionChanged(it)) }
                )
            }
            item {
                AmountInputField(
                    value = transaction.amount.toPlainString(),
                    onValueChange = { onEvent(ScanReceiptEvent.AmountChanged(it)) }
                )
            }
            item {
                FormSelectorField(
                    label = "Category",
                    value = transaction.category?.name ?: "Choose category",
                    onClick = { onEvent(ScanReceiptEvent.CategoryClicked) },
                    leadingIconRes = transaction.category?.iconIdentifier,
                )
            }
            item {
                FormSelectorField(
                    label = "Account",
                    value = transaction.account.name,
                    onClick = { onEvent(ScanReceiptEvent.AccountClicked) },
                    leadingIconRes = transaction.account.iconIdentifier,
                )
            }
            item {
                DatePickerField(
                    label = "Date",
                    value = transaction.date.toLocalDate(),
                    onClick = { onEvent(ScanReceiptEvent.DateClicked) },
                    mode = DatePickerMode.FULL_DATE
                )
            }

            // --- BAGIAN COLLAPSIBLE ---
            item {
                CollapsibleSection(
                    title = "Scanned Items (${transaction.receiptItemModels.size})",
                    isExpanded = uiState.isItemListExpanded,
                    onToggle = { onEvent(ScanReceiptEvent.ToggleItemListExpansion) }
                ) {
                    // Konten ini hanya akan terlihat jika isExpanded = true
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        transaction.receiptItemModels.forEachIndexed { index, item ->
                            EditableReceiptItemRow(
                                index = index,
                                item = item,
                                onEvent = onEvent
                            )
                        }
                        TextButton(onClick = { onEvent(ScanReceiptEvent.AddLineItem) }) {
                            Icon(Icons.Default.Add, contentDescription = "Add Item")
                            Spacer(Modifier.width(8.dp))
                            Text("Add Item")
                        }
                    }
                }
            }

            item {
                CollapsibleSection(
                    title = "Original Receipt",
                    isExpanded = uiState.isReceiptImageExpanded,
                    onToggle = { onEvent(ScanReceiptEvent.ToggleReceiptImageExpansion) }
                ) {
                    AsyncImage(
                        model = uiState.imagePreviewUri,
                        contentDescription = "Receipt Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.FillWidth
                    )
                }
            }
        }
    }
}

// Contoh Composable untuk satu baris item
@Composable
fun ReceiptItemRow(item: ReceiptItemModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "${item.quantity}x ${item.name}", modifier = Modifier.weight(1f))
        Text(text = "Rp ${item.price}")
    }
}

@Composable
fun EditableReceiptItemRow(
    index: Int,
    item: ReceiptItemModel,
    onEvent: (ScanReceiptEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = item.quantity.toString(),
            onValueChange = {
                onEvent(
                    ScanReceiptEvent.LineItemQuantityChanged(
                        index,
                        it.toInt()
                    )
                )
            },
            modifier = Modifier.width(60.dp),
            label = { Text("Qty") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = item.name,
            onValueChange = { onEvent(ScanReceiptEvent.LineItemNameChanged(index, it)) },
            modifier = Modifier.weight(1f),
            label = { Text("Item Name") }
        )
        OutlinedTextField(
            value = item.price.toPlainString(),
            onValueChange = { onEvent(ScanReceiptEvent.LineItemPriceChanged(index, it)) },
            modifier = Modifier.width(100.dp),
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        IconButton(onClick = { onEvent(ScanReceiptEvent.DeleteLineItem(index)) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Item")
        }
    }
}

@Composable
fun CollapsibleSection(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Toggle"
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                content()
            }
        }
    }
}