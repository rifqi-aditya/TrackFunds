package com.rifqi.trackfunds.feature.transaction.ui.addEdit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.ui.components.AppTopAppBar
import com.rifqi.trackfunds.core.ui.components.CustomDatePickerDialog
import com.rifqi.trackfunds.core.ui.components.SelectionItemData
import com.rifqi.trackfunds.core.ui.components.SelectionList
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerMode
import com.rifqi.trackfunds.core.ui.components.inputfield.FormSelectorField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.core.ui.preview.DummyData
import com.rifqi.trackfunds.core.ui.theme.TrackFundsTheme
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.transaction.ui.components.AnimatedSlideToggleButton
import com.rifqi.trackfunds.feature.transaction.ui.components.TransactionDetailsSection
import com.rifqi.trackfunds.feature.transaction.ui.model.TransactionTypes

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
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoriesForSelection by viewModel.categoriesForSelection.collectAsState()

    CustomDatePickerDialog(
        showDialog = uiState.showDatePicker,
        initialDate = uiState.selectedDate,
        onDismiss = {
            viewModel.onEvent(AddEditTransactionEvent.DismissDatePicker)
        },
        onConfirm = { newDate ->
            viewModel.onEvent(AddEditTransactionEvent.DateSelected(newDate))
        }
    )

    if (uiState.activeSheet != null) {
        ModalBottomSheet(onDismissRequest = { viewModel.onEvent(AddEditTransactionEvent.DismissSheet) }) {
            when (uiState.activeSheet) {
                AddEditSheetType.CATEGORY -> {
                    SelectionList(
                        title = "Select Category",
                        items = categoriesForSelection.map { category ->
                            SelectionItemData(
                                id = category.id,
                                title = category.name,
                                iconIdentifier = category.iconIdentifier
                            )
                        },
                        selectedItemId = uiState.selectedCategory?.id,
                        onItemSelected = { categoryId ->
                            val selected = categoriesForSelection.find { it.id == categoryId }
                            selected?.let {
                                viewModel.onEvent(
                                    AddEditTransactionEvent.CategorySelected(
                                        it
                                    )
                                )
                            }
                        },
                        isSearchable = true,
                        searchQuery = uiState.categorySearchQuery,
                        onSearchQueryChanged = { query ->
                            viewModel.onEvent(AddEditTransactionEvent.CategorySearchChanged(query))
                        },
                    )
                }

                AddEditSheetType.ACCOUNT -> {
                    SelectionList(
                        title = "Select Account",
                        items = uiState.allAccounts.map { account ->
                            SelectionItemData(
                                id = account.id,
                                title = account.name,
                                subtitle = formatCurrency(account.balance),
                                iconIdentifier = account.iconIdentifier
                            )
                        },
                        selectedItemId = uiState.selectedAccount?.id,
                        onItemSelected = { accountId ->
                            val selected = uiState.allAccounts.find { it.id == accountId }
                            selected?.let {
                                viewModel.onEvent(
                                    AddEditTransactionEvent.AccountSelected(
                                        it
                                    )
                                )
                            }
                        },
                    )
                }

                AddEditSheetType.SAVINGS_GOAL -> {
                    SelectionList(
                        title = "Select Savings Goal",
                        items = uiState.allSavingsGoals.map { savings ->
                            // Map dari SavingsGoal ke SelectionItemData
                            SelectionItemData(
                                id = savings.id,
                                title = savings.name,
                                subtitle = "Saved: ${formatCurrency(savings.savedAmount)}",
                                iconIdentifier = savings.iconIdentifier
                            )
                        },
                        selectedItemId = uiState.selectedSavingsGoal?.id,
                        onItemSelected = { savingsId ->
                            val selected = uiState.allSavingsGoals.find { it.id == savingsId }
                            selected?.let {
                                viewModel.onEvent(
                                    AddEditTransactionEvent.SavingsGoalSelected(
                                        it
                                    )
                                )
                            }
                        }
                    )
                }

                null -> TODO()
            }
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onEvent(AddEditTransactionEvent.OnReceiptImageSelected(it)) }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is AddEditTransactionSideEffect.NavigateBack -> onNavigateBack()
                is AddEditTransactionSideEffect.LaunchGallery -> galleryLauncher.launch("image/*")
                is AddEditTransactionSideEffect.ShowSnackbar -> {}
            }
        }
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
            )
        },
        bottomBar = {
            Button(
                onClick = { onEvent(AddEditTransactionEvent.SaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = TrackFundsTheme.extendedColors.accent,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
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
                .padding(
                    top = innerPadding.calculateTopPadding() + 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AnimatedSlideToggleButton(
                items = TransactionTypes,
                selectedItem = uiState.selectedTransactionType,
                onItemSelected = { onEvent(AddEditTransactionEvent.TypeChanged(it)) },
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
        isError = uiState.amountError != null,
        errorMessage = uiState.amountError,
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
        mode = DatePickerMode.FULL_DATE
    )

    GeneralTextInputField(
        value = uiState.description,
        onValueChange = { onEvent(AddEditTransactionEvent.DescriptionChanged(it)) },
        label = "Description (Optional)",
        placeholder = "Enter transaction description",
        singleLine = false,
    )

    AnimatedVisibility(
        visible = uiState.selectedTransactionType == TransactionType.EXPENSE,
        enter = expandVertically(
            expandFrom = Alignment.Top,
            animationSpec = tween(durationMillis = 300)
        ) + fadeIn(
            animationSpec = tween(durationMillis = 150)
        ),
        exit = shrinkVertically(
            shrinkTowards = Alignment.Top,
            animationSpec = tween(durationMillis = 300)
        ) + fadeOut(
            animationSpec = tween(durationMillis = 150)
        )
    ) {
        TransactionDetailsSection(
            isItemsExpanded = uiState.isItemsExpanded,
            isReceiptExpanded = uiState.isReceiptExpanded,
            items = uiState.items,
            receiptImageUri = uiState.receiptImageUri,
            onEvent = onEvent
        )
    }

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
        value = uiState.selectedSavingsGoal?.name ?: "Choose savings goal",
        onClick = { onEvent(AddEditTransactionEvent.SavingsGoalSelectorClicked) },
        leadingIconRes = uiState.selectedSavingsGoal?.iconIdentifier,
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
        mode = DatePickerMode.FULL_DATE
    )

    GeneralTextInputField(
        value = uiState.description,
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

@Preview(showBackground = true, name = "Add Mode")
@Composable
fun AddTransactionContentLightPreview() {
    TrackFundsTheme {
        AddEditTransactionContent(
            uiState = AddEditTransactionUiState(
                selectedCategory = DummyData.dummyCategory1,
                selectedAccount = DummyData.dummyAccount1
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
                selectedCategory = DummyData.dummyCategory2,
                selectedAccount = DummyData.dummyAccount2,
                selectedTransactionType = TransactionType.SAVINGS
            ),
            isEditMode = true,
            onEvent = { },
            onNavigateBack = { },
        )
    }
}