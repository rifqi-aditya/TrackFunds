package com.rifqi.trackfunds.feature.transaction.ui.model

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.time.LocalDate

data class AddEditTransactionUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isTransactionSaved: Boolean = false,

    // Data Form
    val amount: String = "",
    val selectedTransactionType: TransactionType = TransactionType.EXPENSE,
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedAccount: AccountItem? = null,
    val selectedCategory: CategoryItem? = null,
    val notes: String = "",

    // UI State
    val showDatePicker: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false
)