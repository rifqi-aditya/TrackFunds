package com.rifqi.trackfunds.feature.transaction.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
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
import com.rifqi.trackfunds.core.ui.components.AmountInputForm
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.CustomDatePickerDialog
import com.rifqi.trackfunds.core.ui.components.FormSelectorCard
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.transaction.ui.components.DateTimeDisplayRow
import com.rifqi.trackfunds.feature.transaction.ui.components.descriptionsInputField
import com.rifqi.trackfunds.feature.transaction.ui.components.TransactionTypeToggleButtons
import com.rifqi.trackfunds.feature.transaction.ui.event.AddEditTransactionEvent
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
@Composable
fun AddEditTransactionScreen(
    viewModel: AddEditTransactionViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigate: (AppScreen) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                    onClick = { viewModel.onConfirmDelete() },
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
                title = {
                    Text(
                        if (isEditMode) "Edit Transaction" else "Add Transaction",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = "Back",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = { onEvent(AddEditTransactionEvent.DeleteClicked) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete Transaction"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(0.dp))

            DateTimeDisplayRow(
                selectedDate = uiState.selectedDate,
                onClick = { onEvent(AddEditTransactionEvent.DateSelectorClicked) }
            )

            TransactionTypeToggleButtons(
                selectedType = uiState.selectedTransactionType,
                onTypeSelected = { onEvent(AddEditTransactionEvent.TransactionTypeChanged(it)) }
            )

            FormSelectorCard(
                label = "Account",
                value = uiState.selectedAccount?.name ?: "Choose account",
                onClick = { onEvent(AddEditTransactionEvent.AccountSelectorClicked) },
                leadingIconRes = uiState.selectedAccount?.iconIdentifier,
            )

            AmountInputForm(
                value = uiState.amount,
                onValueChange = { onEvent(AddEditTransactionEvent.AmountChanged(it)) },
            )

            descriptionsInputField( // Menggunakan input field biasa, bukan selector
                value = uiState.descriptions,
                onValueChange = { onEvent(AddEditTransactionEvent.descriptionChanged(it)) }
            )

            uiState.selectedCategory?.let {
                // Jika kategori sudah dipilih, tampilkan detailnya
                FormSelectorCard(
                    label = "Category",
                    value = it.name,
                    onClick = { onEvent(AddEditTransactionEvent.CategorySelectorClicked) },
                    leadingIconRes = it.iconIdentifier
                )
            } ?: // Jika belum ada kategori yang dipilih, tampilkan placeholder
            FormSelectorCard(
                label = "Category",
                value = "Choose a category",
                onClick = { onEvent(AddEditTransactionEvent.CategorySelectorClicked) },
                leadingIconRes = ""
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
            Button(
                onClick = { onEvent(AddEditTransactionEvent.SaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .height(56.dp),
                shape = MaterialTheme.shapes.large,
                enabled = !uiState.isLoading
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
                selectedAccount = previewDummyAccount
            ),
            isEditMode = true,
            onEvent = { },
            onNavigateBack = { },
        )
    }
}