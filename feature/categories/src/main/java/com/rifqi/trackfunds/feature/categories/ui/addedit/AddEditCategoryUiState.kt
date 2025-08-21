package com.rifqi.trackfunds.feature.categories.ui.addedit

import com.rifqi.trackfunds.core.domain.category.model.TransactionType

data class AddEditCategoryUiState(
    // identity
    val id: String? = null,
    val type: TransactionType = TransactionType.EXPENSE,

    // form
    val name: String = "",
    val iconIdentifier: String = "",

    // validation
    val nameError: String? = null,
    val canSubmit: Boolean = false,

    // ui flags
    val isLoading: Boolean = false, // untuk load data awal (edit)
    val isSaving: Boolean = false,  // untuk proses simpan
    val isIconSheetVisible: Boolean = false,
    val canDelete: Boolean = false,
    val screenTitle: String = "Add Category",

    // error global (optional untuk debug/log)
    val errorMessage: String? = null
)