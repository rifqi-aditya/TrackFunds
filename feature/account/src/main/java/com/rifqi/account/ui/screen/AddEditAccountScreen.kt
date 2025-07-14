package com.rifqi.account.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
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
import com.rifqi.account.ui.event.AddEditAccountEvent
import com.rifqi.account.ui.model.DefaultAccountsIcons
import com.rifqi.account.ui.state.AddEditAccountUiState
import com.rifqi.account.ui.viewmodel.AddEditAccountViewModel
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

    LaunchedEffect(uiState.isAccountSaved) {
        if (uiState.isAccountSaved) {
            onNavigateBack()
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
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditAccountContent(
    uiState: AddEditAccountUiState,
    onEvent: (AddEditAccountEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = if (uiState.isEditMode) "Edit Account" else "Add New Account",
                isFullScreen = true,
                onNavigateBack = onNavigateBack,
                actions = {
                    if (uiState.isEditMode) {
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
        }
    ) { paddingValues ->
        val iconBorderColor = if (uiState.iconError != null) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.outline
        }

        Column(
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding(), bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),

            ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.dp,
                            color = iconBorderColor,
                            shape = CircleShape
                        )
                        .clickable { onEvent(AddEditAccountEvent.ShowIconPickerClicked) },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.iconIdentifier.isNotBlank()) {
                        DisplayIconFromResource(
                            identifier = uiState.iconIdentifier,
                            contentDescription = "Savings Icon",
                            modifier = Modifier.size(48.dp)
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.AddAPhoto,
                                contentDescription = null,
                                tint = if (uiState.iconError != null) MaterialTheme.colorScheme.error else LocalContentColor.current
                            )
                            Text("Select an icon", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }

                GeneralTextInputField(
                    value = uiState.name,
                    onValueChange = { onEvent(AddEditAccountEvent.NameChanged(it)) },
                    label = "Account Name",
                    isError = uiState.nameError != null,
                    errorMessage = uiState.nameError,
                    modifier = Modifier.fillMaxWidth()
                )

                AmountInputField(
                    value = uiState.initialBalance,
                    onValueChange = { onEvent(AddEditAccountEvent.BalanceChanged(it)) },
                    label = "Initial Balance",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onEvent(AddEditAccountEvent.SaveAccountClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !uiState.isSaving
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Save Account")
                    }
                }
            }
        }
    }
}