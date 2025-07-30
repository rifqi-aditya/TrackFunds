package com.rifqi.trackfunds.feature.transaction.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rifqi.trackfunds.core.ui.components.inputfield.AmountInputField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerField
import com.rifqi.trackfunds.core.ui.components.inputfield.DatePickerMode
import com.rifqi.trackfunds.core.ui.components.inputfield.FormSelectorField
import com.rifqi.trackfunds.core.ui.components.inputfield.GeneralTextInputField
import com.rifqi.trackfunds.feature.transaction.ui.addEditTransaction.AddEditTransactionEvent
import com.rifqi.trackfunds.feature.transaction.ui.addEditTransaction.AddEditTransactionUiState

@Composable
fun ExpenseFormContent(
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
        mode = DatePickerMode.FULL_DATE
    )

    GeneralTextInputField(
        value = uiState.description,
        onValueChange = { onEvent(AddEditTransactionEvent.DescriptionChanged(it)) },
        label = "Description",
        placeholder = "Enter transaction description",
        singleLine = false,
    )

    TransactionDetailsSection(
        isExpanded = uiState.isDetailsExpanded,
        lineItems = uiState.lineItems,
        receiptImageUri = uiState.receiptImageUri,
        onEvent = onEvent
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