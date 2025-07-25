package com.rifqi.trackfunds.feature.budget.ui.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.feature.budget.ui.state.AddEditBudgetUiState
import java.time.YearMonth

class AddEditBudgetUiStatePreviewParameterProvider : CollectionPreviewParameterProvider<AddEditBudgetUiState>(
    listOf(
        // Skenario Default / Tambah Baru
        AddEditBudgetUiState(
            period = YearMonth.now(),
            amount = "1000000",
            selectedCategory = CategoryItem(
                id = "1",
                name = "Makanan & Minuman",
                iconIdentifier = "ic_food", // Ganti dengan nama resource ikon yang valid
                type = TransactionType.EXPENSE
            ),
            isLoading = false,
            showPeriodPicker = false,
            showCategorySheet = false,
            categorySearchQuery = "",
            showDeleteConfirmDialog = false
        ),
        // Skenario Edit dengan beberapa data terisi
        AddEditBudgetUiState(
            period = YearMonth.now().minusMonths(1),
            amount = "750000",
            selectedCategory = CategoryItem(
                id = "2",
                name = "Transportasi",
                iconIdentifier = "ic_transportation", // Ganti dengan nama resource ikon yang valid
                type = TransactionType.EXPENSE
            ),
            isLoading = false,
            showPeriodPicker = false,
            showCategorySheet = false,
            categorySearchQuery = "",
            showDeleteConfirmDialog = false
        ),
        // Skenario Loading
        AddEditBudgetUiState(
            period = YearMonth.now(),
            amount = "500000",
            selectedCategory = CategoryItem(
                id = "3",
                name = "Belanja",
                iconIdentifier = "ic_shopping", // Ganti dengan nama resource ikon yang valid
                type = TransactionType.EXPENSE
            ),
            isLoading = true,
            showPeriodPicker = false,
            showCategorySheet = false,
            categorySearchQuery = "",
            showDeleteConfirmDialog = false
        ),
        // Skenario dengan Period Picker Ditampilkan
        AddEditBudgetUiState(
            period = YearMonth.now(),
            amount = "200000",
            selectedCategory = null, // Belum ada kategori dipilih
            isLoading = false,
            showPeriodPicker = true,
            showCategorySheet = false,
            categorySearchQuery = "",
            showDeleteConfirmDialog = false
        ),
        // Skenario dengan Category Sheet Ditampilkan
        AddEditBudgetUiState(
            period = YearMonth.now(),
            amount = "300000",
            selectedCategory = null,
            isLoading = false,
            showPeriodPicker = false,
            showCategorySheet = true,
            categorySearchQuery = "Mak",
            showDeleteConfirmDialog = false
        )
    )
)