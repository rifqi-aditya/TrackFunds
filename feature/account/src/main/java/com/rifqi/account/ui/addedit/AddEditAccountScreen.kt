package com.rifqi.account.ui.addedit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.account.ui.model.DefaultAccountsIcons
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.IconPicker
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.utils.DisplayIconFromResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAccountScreen(
    viewModel: AddEditAccountViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(true) {
        viewModel.sideEffect.collect {
            when (it) {
                is AddEditAccountSideEffect.NavigateBack -> onNavigateBack()
                is AddEditAccountSideEffect.ShowSnackbar -> {

                }
            }
        }
    }
    if (uiState.showIconPicker) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(AddEditAccountEvent.IconPickerDismissed) },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Please Select an Icon",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                IconPicker(
                    icons = DefaultAccountsIcons.list,
                    selectedIconIdentifier = uiState.iconIdentifier,
                    onIconSelected = { identifier ->
                        viewModel.onEvent(AddEditAccountEvent.IconIdentifierChanged(identifier))
                    },
                )
            }

        }
    }

    if (uiState.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(AddEditAccountEvent.DismissDeleteDialog) },
            title = { Text("Hapus Akun") },
            text = { Text("Apakah Anda yakin ingin menghapus akun ini? Semua transaksi terkait akan ikut terhapus.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(AddEditAccountEvent.ConfirmDeleteClicked) }) {
                    Text("Hapus", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(AddEditAccountEvent.DismissDeleteDialog) }) {
                    Text("Batal")
                }
            }
        )
    }

    AddEditAccountContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
        isEditMode = viewModel.isEditMode
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAccountContent(
    uiState: AddEditAccountUiState,
    onEvent: (AddEditAccountEvent) -> Unit,
    onNavigateBack: () -> Unit,
    isEditMode: Boolean
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = if (isEditMode) "Edit Account" else "Add New Account",
                isFullScreen = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = { onEvent(AddEditAccountEvent.DeleteClicked) }) {
                            DisplayIconFromResource(
                                identifier = "delete",
                                contentDescription = "Delete Account",
                                modifier = Modifier
                                    .padding(end = 8.dp, start = 4.dp)
                                    .size(24.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Button(
                    onClick = { onEvent(AddEditAccountEvent.SaveAccountClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isSaving
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        Text(if (isEditMode) "Save Changes" else "Create Account")
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp)
        ) {
            // Bagian loading
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
                return@LazyColumn
            }

            // Icon Picker
            item {
                IconPicker(
                    iconIdentifier = uiState.iconIdentifier,
                    iconError = uiState.iconError,
                    onIconClicked = { onEvent(AddEditAccountEvent.ShowIconPickerClicked) }
                )
            }

            // Form Fields
            item {
                GeneralTextInputField(
                    value = uiState.name,
                    onValueChange = { onEvent(AddEditAccountEvent.NameChanged(it)) },
                    label = "Account Name",
                    isError = uiState.nameError != null,
                    errorMessage = uiState.nameError,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 2. Tampilkan Saldo Awal hanya saat mode Tambah
            if (!isEditMode) {
                item {
                    AmountInputField(
                        value = uiState.initialBalance,
                        onValueChange = { onEvent(AddEditAccountEvent.BalanceChanged(it)) },
                        label = "Initial Balance",
                        isError = uiState.balanceError != null,
                        errorMessage = uiState.balanceError,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 3. Pindahkan tombol Hapus ke bawah (hanya di mode edit)
            if (isEditMode) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { onEvent(AddEditAccountEvent.DeleteClicked) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Delete Account", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun IconPicker(
    iconIdentifier: String,
    iconError: String?,
    onIconClicked: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (iconError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
        label = "iconBorderColor"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                .clickable(onClick = onIconClicked),
            contentAlignment = Alignment.Center
        ) {
            if (iconIdentifier.isNotBlank()) {
                DisplayIconFromResource(
                    identifier = iconIdentifier,
                    contentDescription = "Selected Icon",
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.AddAPhoto, contentDescription = null)
                    Text("Select Icon", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        AnimatedVisibility(visible = iconError != null) {
            Text(
                text = iconError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}