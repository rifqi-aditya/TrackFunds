package com.rifqi.account.ui.addedit

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.account.ui.addedit.components.IconPicker
import com.rifqi.account.ui.model.DefaultAccountsIcons
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.IconPickerSheet
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme

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

                IconPickerSheet(
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
            title = { Text("Delete Account") },
            text = { Text("Are you sure you want to delete this account? All related transactions will also be deleted.") },
            confirmButton = {
                TextButton(onClick = { viewModel.onEvent(AddEditAccountEvent.ConfirmDeleteClicked) }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(AddEditAccountEvent.DismissDeleteDialog) }) {
                    Text("Cancel")
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
                    shape = MaterialTheme.shapes.medium,
                    enabled = !uiState.isSaving,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TrackFundsTheme.extendedColors.accent,
                        contentColor = TrackFundsTheme.extendedColors.onAccent
                    )
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TrackFundsTheme.extendedColors.onAccent
                        )
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
            if (uiState.isLoading) {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                }
                return@LazyColumn
            }

            item {
                IconPicker(
                    iconIdentifier = uiState.iconIdentifier,
                    iconError = uiState.iconError,
                    onIconClicked = { onEvent(AddEditAccountEvent.ShowIconPickerClicked) }
                )
            }

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

            if (isEditMode) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { onEvent(AddEditAccountEvent.DeleteClicked) },
                    ) {
                        Text("Delete Account", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}