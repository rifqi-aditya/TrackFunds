package com.rifqi.trackfunds.feature.budget.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.MonthYearPickerDialog
import com.rifqi.trackfunds.core.ui.components.SelectionList
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerMode
import com.rifqi.trackfunds.core.ui.components.inputfield.FormSelectorField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.budget.ui.event.AddEditBudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.state.AddEditBudgetUiState
import com.rifqi.trackfunds.feature.budget.ui.viewmodel.AddEditBudgetViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.YearMonth

/**
 * Stateful Composable (Container)
 * - Menerima ViewModel dari NavGraph.
 * - Menangani event navigasi dan dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBudgetScreen(
    viewModel: AddEditBudgetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSelectCategory: (period: String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoriesForSelection by viewModel.categoriesForSelection.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest { periodString ->
            onNavigateToSelectCategory(periodString)
        }
    }

    LaunchedEffect(uiState.isBudgetSaved) {
        if (uiState.isBudgetSaved) {
            onNavigateBack()
        }
    }

    if (uiState.showPeriodPicker) {
        MonthYearPickerDialog(
            showDialog = true,
            initialYearMonth = uiState.period,
            onDismiss = { viewModel.onEvent(AddEditBudgetEvent.DismissPeriodPicker) },
            onConfirm = { selectedYearMonth ->
                viewModel.onEvent(AddEditBudgetEvent.PeriodSelected(selectedYearMonth))
            }
        )
    }

    if (uiState.showCategorySheet) {
        ModalBottomSheet(onDismissRequest = { viewModel.onEvent(AddEditBudgetEvent.DismissCategorySheet) }) {
            SelectionList(
                title = "Pilih Kategori",
                items = categoriesForSelection,
                itemBuilder = { category ->
                    com.rifqi.trackfunds.core.ui.components.SelectionItem(
                        category.id,
                        category.name,
                        category.iconIdentifier
                    )
                },
                onItemSelected = { category ->
                    viewModel.onEvent(AddEditBudgetEvent.CategorySelected(category))
                },
                isSearchable = true,
                searchQuery = uiState.categorySearchQuery,
                onSearchQueryChanged = { query ->
                    viewModel.onEvent(AddEditBudgetEvent.CategorySearchChanged(query))
                },
            )
        }
    }

// Tampilkan dialog konfirmasi hapus jika state-nya true
    if (uiState.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(AddEditBudgetEvent.DismissDeleteDialog) },
            title = { Text("Delete Budget") },
            text = { Text("Are you sure you want to delete this budget for this period?") },
            confirmButton = {
                Button(
                    onClick = { viewModel.onEvent(AddEditBudgetEvent.ConfirmDeleteClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(AddEditBudgetEvent.DismissDeleteDialog) }) {
                    Text(
                        "Cancel"
                    )
                }
            }
        )
    }

// Panggil UI murni (stateless)
    AddEditBudgetContent(
        uiState = uiState,
        isEditMode = viewModel.isEditMode,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
    )
}

/**
 * Stateless Composable (Presentational)
 * - Hanya menerima state dan lambda event. Sangat mudah di-preview.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBudgetContent(
    uiState: AddEditBudgetUiState,
    isEditMode: Boolean,
    onEvent: (AddEditBudgetEvent) -> Unit,
    onNavigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            AppTopAppBar(
                title = if (isEditMode) "Edit Budget" else "Add Budget",
                onNavigateBack = onNavigateBack,
                isFullScreen = true,
                actions = {
                    if (isEditMode) {
                        IconButton(onClick = { onEvent(AddEditBudgetEvent.DeleteClicked) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete Budget")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Form untuk Kategori
            FormSelectorField(
                label = "Category",
                value = uiState.selectedCategory?.name ?: "Choose a category",
                leadingIconRes = uiState.selectedCategory?.iconIdentifier,
                // Kategori tidak bisa diubah saat mode edit
                onClick = { if (!isEditMode) onEvent(AddEditBudgetEvent.CategorySelectorClicked) },
//                enabled = !isEditMode
            )

            // Form untuk Jumlah
            AmountInputField(
                value = uiState.amount,
                onValueChange = { onEvent(AddEditBudgetEvent.AmountChanged(it)) }
            )

            DatePickerField(
                label = "Choose Month",
                value = uiState.period,
                onClick = { onEvent(AddEditBudgetEvent.PeriodSelectorClicked) },
                mode = DatePickerMode.MONTH_YEAR_ONLY
            )

            Spacer(modifier = Modifier.weight(1f))

            // Tombol Simpan
            Button(
                onClick = { onEvent(AddEditBudgetEvent.SaveBudgetClicked) },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                if (uiState.isLoading) CircularProgressIndicator() else Text(if (isEditMode) "Save Changes" else "Save Budget")
            }
        }
    }
}

// --- DUMMY DATA UNTUK KEPERLUAN PREVIEW ---

private val previewBudgetCategory = CategoryItem(
    id = "cat_food",
    name = "Food & Drink",
    iconIdentifier = "restaurant",
    type = TransactionType.EXPENSE
)

// --- FUNGSI-FUNGSI PREVIEW ---

@Preview(showBackground = true, name = "Add Budget - Empty State")
@Composable
private fun AddBudgetContentEmptyPreview() {
    TrackFundsTheme {
        AddEditBudgetContent(
            uiState = AddEditBudgetUiState(period = YearMonth.now()),
            isEditMode = false,
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Add Budget - Filled State")
@Composable
private fun AddBudgetContentFilledPreview() {
    TrackFundsTheme {
        AddEditBudgetContent(
            uiState = AddEditBudgetUiState(
                period = YearMonth.now(),
                selectedCategory = previewBudgetCategory,
                amount = "750000"
            ),
            isEditMode = false,
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Edit Budget Mode")
@Preview(
    showBackground = true,
    name = "Edit Budget Mode (Dark)",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun EditBudgetContentPreview() {
    TrackFundsTheme {
        AddEditBudgetContent(
            uiState = AddEditBudgetUiState(
                period = YearMonth.now(),
                selectedCategory = previewBudgetCategory,
                amount = "1200000"
            ),
            isEditMode = true, // <-- Mensimulasikan mode edit
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, name = "Add Budget - Loading State")
@Composable
private fun AddBudgetContentLoadingPreview() {
    TrackFundsTheme {
        AddEditBudgetContent(
            uiState = AddEditBudgetUiState(
                period = YearMonth.now(),
                selectedCategory = previewBudgetCategory,
                amount = "1200000",
                isLoading = true // <-- Mensimulasikan kondisi loading
            ),
            isEditMode = false,
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

