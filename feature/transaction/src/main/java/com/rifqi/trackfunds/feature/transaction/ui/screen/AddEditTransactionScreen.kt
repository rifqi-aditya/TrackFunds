package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.Home
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.CustomDatePickerDialog
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.FormSelectorField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.transaction.ui.components.AnimatedSlideToggleButton
import com.rifqi.trackfunds.feature.transaction.ui.components.SavingsGoalSelectionRow
import com.rifqi.trackfunds.feature.transaction.ui.event.AddEditTransactionEvent
import com.rifqi.trackfunds.feature.transaction.ui.model.transactionTypes
import com.rifqi.trackfunds.feature.transaction.ui.state.AddEditTransactionUiState
import com.rifqi.trackfunds.feature.transaction.ui.viewmodel.AddEditTransactionViewModel
import kotlinx.coroutines.flow.collectLatest
import java.math.BigDecimal

/**
 * Stateful Composable (Container)
 * - Menerima ViewModel dari NavGraph.
 * - Menangani event UI dan meneruskannya ke ViewModel.
 * - Menampilkan dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    viewModel: AddEditTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val savingsGoals = viewModel.savingsGoals.collectAsState(initial = emptyList()).value
    val sheetState = rememberModalBottomSheetState()

    CustomDatePickerDialog(
        showDialog = uiState.showDatePicker,
        initialDate = uiState.selectedDate,
        onDismiss = {
            viewModel.onDatePickerDismissed()
        },
        onConfirm = { newDate ->
            viewModel.onDateSelected(newDate)
        }
    )

    if (uiState.showSavingsGoalSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(AddEditTransactionEvent.SavingsGoalSheetDismissed) },
            sheetState = sheetState
        ) {
            // Isi dari BottomSheet
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    "Pilih Tujuan Tabungan",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(savingsGoals) { goal ->
                        SavingsGoalSelectionRow(
                            goal = goal,
                            onClick = {
                                viewModel.onEvent(AddEditTransactionEvent.SavingsGoalSelected(goal))
                            }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { screen ->
            if (screen is Home) {
                onNavigateBack()
            } else {
                onNavigate(screen)
            }
        }
    }

    if (uiState.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onDismissDeleteDialog() },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(AddEditTransactionEvent.ConfirmDeleteClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onDismissDeleteDialog() }) { Text("Cancel") }
            }
        )
    }

    AddEditTransactionContent(
        uiState = uiState,
        isEditMode = viewModel.isEditMode,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack
    )
}

/**
 * Stateless Composable (Presentational)
 * - Hanya menerima state dan lambda.
 * - Tidak tahu tentang ViewModel atau Hilt.
 * - Sangat mudah untuk di-preview dan diuji.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionContent(
    uiState: AddEditTransactionUiState,
    isEditMode: Boolean,
    onEvent: (AddEditTransactionEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = if (isEditMode) "Edit Transaction" else "Add Transaction",
                onNavigateBack = onNavigateBack,
                isFullScreen = true,
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = { onEvent(AddEditTransactionEvent.DeleteClicked) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Transaction"
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(48.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { onEvent(AddEditTransactionEvent.SaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        if (isEditMode) "Save Changes" else "Save Transaction",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedSlideToggleButton(
                items = transactionTypes,
                selectedItem = uiState.selectedTransactionType,
                onItemSelected = { onEvent(AddEditTransactionEvent.TransactionTypeChanged(it)) },
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (uiState.selectedTransactionType) {
                    TransactionType.SAVINGS -> {
                        SavingsFormContent(uiState = uiState, onEvent = onEvent)
                    }

                    else -> {
                        StandardFormContent(uiState = uiState, onEvent = onEvent)
                    }
                }
            }
        }
    }
}


@Composable
private fun StandardFormContent(
    uiState: AddEditTransactionUiState,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    AmountInputField(
        value = uiState.amount,
        placeholder = "0",
        onValueChange = { onEvent(AddEditTransactionEvent.AmountChanged(it)) },
    )

    FormSelectorField(
        label = "Category",
        value = uiState.selectedCategory?.name ?: "Choose category",
        onClick = { onEvent(AddEditTransactionEvent.CategorySelectorClicked) },
        leadingIconRes = uiState.selectedCategory?.iconIdentifier,
    )

    FormSelectorField(
        label = "Source Account",
        value = uiState.selectedAccount?.name ?: "Choose account",
        onClick = { onEvent(AddEditTransactionEvent.AccountSelectorClicked) },
        leadingIconRes = uiState.selectedAccount?.iconIdentifier,
    )

    DatePickerField(
        label = "Date",
        value = uiState.selectedDate,
        onClick = { onEvent(AddEditTransactionEvent.DateSelectorClicked) },
    )

    GeneralTextInputField(
        value = uiState.descriptions,
        onValueChange = { onEvent(AddEditTransactionEvent.DescriptionChanged(it)) },
        label = "Description",
        placeholder = "Enter transaction description",
        singleLine = false,
    )

    uiState.error?.let { errorMessage ->
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}

@Composable
private fun SavingsFormContent(
    uiState: AddEditTransactionUiState,
    onEvent: (AddEditTransactionEvent) -> Unit
) {
    FormSelectorField(
        label = "Savings Goal",
        value = uiState.selectedSavingsGoalItem?.name ?: "Choose savings goal",
        onClick = { onEvent(AddEditTransactionEvent.SavingsGoalSelectorClicked) },
        leadingIconRes = uiState.selectedSavingsGoalItem?.iconIdentifier,
    )

    FormSelectorField(
        label = "Source Account",
        value = uiState.selectedAccount?.name ?: "Choose account",
        onClick = { onEvent(AddEditTransactionEvent.AccountSelectorClicked) },
        leadingIconRes = uiState.selectedAccount?.iconIdentifier,
    )

    AmountInputField(
        value = uiState.amount,
        placeholder = "0",
        onValueChange = { onEvent(AddEditTransactionEvent.AmountChanged(it)) },
    )

    DatePickerField(
        label = "Date",
        value = uiState.selectedDate,
        onClick = { onEvent(AddEditTransactionEvent.DateSelectorClicked) },
    )

    GeneralTextInputField(
        value = uiState.descriptions,
        onValueChange = { onEvent(AddEditTransactionEvent.DescriptionChanged(it)) },
        label = "Description",
        placeholder = "Enter transaction description",
        singleLine = false,
    )
    uiState.error?.let { errorMessage ->
        Text(
            text = errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )
    }
}


// --- PREVIEW sekarang memanggil AddTransactionContent ---

// Data dummy untuk digunakan di preview
private val previewDummyAccount =
    AccountItem(
        id = "acc",
        name = "Mbanking BCA",
        iconIdentifier = "ic_wallet_placeholder",
        balance = BigDecimal("1000000")
    )
private val previewDummyCategory = CategoryItem(
    id = "cat",
    name = "Makanan",
    iconIdentifier = "ic_restaurant",
    type = TransactionType.EXPENSE
)

@Preview(showBackground = true, name = "Add Mode")
@Composable
fun AddTransactionContentLightPreview() {
    TrackFundsTheme {
        AddEditTransactionContent(
            uiState = AddEditTransactionUiState(
                selectedCategory = previewDummyCategory,
                selectedAccount = previewDummyAccount
            ),
            isEditMode = false,
            onEvent = { },
            onNavigateBack = { },
        )
    }
}

@Preview(showBackground = true, name = "Edit Mode")
@Composable
fun EditTransactionContentLightPreview() {
    TrackFundsTheme {
        AddEditTransactionContent(
            uiState = AddEditTransactionUiState(
                amount = "150000",
                selectedCategory = previewDummyCategory,
                selectedAccount = previewDummyAccount,
                selectedTransactionType = TransactionType.SAVINGS
            ),
            isEditMode = true,
            onEvent = { },
            onNavigateBack = { },
        )
    }
}