package com.rifqi.trackfunds.feature.budget.ui.screen

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.MonthYearPickerDialog
import com.rifqi.trackfunds.core.ui.components.SelectionList
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerMode
import com.rifqi.trackfunds.core.ui.components.inputfield.FormSelectorField
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.feature.budget.ui.event.AddEditBudgetEvent
import com.rifqi.trackfunds.feature.budget.ui.preview.AddEditBudgetUiStatePreviewParameterProvider
import com.rifqi.trackfunds.feature.budget.ui.sideeffect.AddEditBudgetSideEffect
import com.rifqi.trackfunds.feature.budget.ui.state.AddEditBudgetUiState
import com.rifqi.trackfunds.feature.budget.ui.viewmodel.AddEditBudgetViewModel
import java.time.YearMonth

/**
 * A screen responsible for adding or editing a budget.
 *
 * @param viewModel The ViewModel that manages the business logic and state of this screen.
 * @param onNavigateBack Callback to navigate back to the previous screen.
 * @param onNavigateToEditMode Callback to navigate to edit mode with a specific budget ID and period.
 */
@Composable
fun AddEditBudgetScreen(
    viewModel: AddEditBudgetViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToEditMode: (budgetId: String, period: YearMonth) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoriesForSelection by viewModel.categoriesForSelection.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AddEditBudgetSideEffect.NavigateBack -> onNavigateBack()
                is AddEditBudgetSideEffect.NavigateToEditMode -> {
                    onNavigateToEditMode(effect.budgetId, effect.period)
                }
            }
        }
    }

    AddEditBudgetContent(
        uiState = uiState,
        isEditMode = viewModel.isEditMode,
        onEvent = viewModel::onEvent,
        onNavigateBack = onNavigateBack,
    )

    AddEditBudgetOverlays(
        uiState = uiState,
        categoriesForSelection = categoriesForSelection,
        onEvent = viewModel::onEvent
    )
}


/**
 * Composable that displays overlays like dialogs and bottom sheets for the Add/Edit Budget screen.
 *
 * @param uiState The current state of the Add/Edit Budget screen.
 * @param categoriesForSelection The list of categories available for selection.
 * @param onEvent Callback to handle events triggered by user interactions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddEditBudgetOverlays(
    uiState: AddEditBudgetUiState,
    categoriesForSelection: List<CategoryModel>,
    onEvent: (AddEditBudgetEvent) -> Unit
) {
    if (uiState.showPeriodPicker) {
        MonthYearPickerDialog(
            showDialog = true,
            initialYearMonth = uiState.period,
            onDismiss = { onEvent(AddEditBudgetEvent.DismissPeriodPicker) },
            onConfirm = { selectedYearMonth ->
                onEvent(AddEditBudgetEvent.PeriodSelected(selectedYearMonth))
            }
        )
    }

    if (uiState.showCategorySheet) {
        ModalBottomSheet(onDismissRequest = { onEvent(AddEditBudgetEvent.DismissCategorySheet) }) {
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
                    onEvent(AddEditBudgetEvent.CategorySelected(category))
                },
                isSearchable = true,
                searchQuery = uiState.categorySearchQuery,
                onSearchQueryChanged = { query ->
                    onEvent(AddEditBudgetEvent.CategorySearchChanged(query))
                },
            )
        }
    }

    if (uiState.showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { onEvent(AddEditBudgetEvent.DismissDeleteDialog) },
            title = { Text("Delete Budget") },
            text = { Text("Are you sure you want to delete this budget for this period?") },
            confirmButton = {
                Button(
                    onClick = { onEvent(AddEditBudgetEvent.ConfirmDeleteClicked) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { onEvent(AddEditBudgetEvent.DismissDeleteDialog) }) {
                    Text("Cancel")
                }
            }
        )
    }
}


/**
 * Composable that displays the main content of the Add/Edit Budget screen.
 *
 * @param uiState The current state of the Add/Edit Budget screen.
 * @param isEditMode Boolean flag indicating if the screen is in edit mode.
 * @param onEvent Callback to handle events triggered by user interactions.
 * @param onNavigateBack Callback to navigate back to the previous screen.
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
                    .height(56.dp)
                    .padding(bottom = 16.dp)
            ) {
                if (uiState.isLoading) CircularProgressIndicator() else Text(if (isEditMode) "Save Changes" else "Save Budget")
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun AddEditBudgetContentPreview(
    @PreviewParameter(AddEditBudgetUiStatePreviewParameterProvider::class) uiState: AddEditBudgetUiState
) {
    TrackFundsTheme {
        AddEditBudgetContent(
            uiState = uiState,
            isEditMode = false,
            onEvent = {},
            onNavigateBack = {}
        )
    }
}

