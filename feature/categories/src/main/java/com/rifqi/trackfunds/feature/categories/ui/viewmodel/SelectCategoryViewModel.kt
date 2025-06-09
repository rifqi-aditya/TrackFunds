package com.rifqi.trackfunds.feature.categories.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

// Definisikan UI State untuk layar ini
data class SelectCategoryUiState(
    val isLoading: Boolean = true,
    val categories: List<CategoryItem> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ambil tipe transaksi dari argumen navigasi sekali saat ViewModel dibuat
    private val transactionTypeToDisplay: TransactionType = try {
        TransactionType.valueOf(
            savedStateHandle.get<String>(ARG_TRANSACTION_TYPE) ?: TransactionType.EXPENSE.name
        )
    } catch (e: IllegalArgumentException) {
        TransactionType.EXPENSE // Fallback jika argumen tidak valid
    }

    // Hanya ada SATU StateFlow yang diekspos ke UI untuk merepresentasikan seluruh state layar.
    private val _uiState = MutableStateFlow(SelectCategoryUiState(isLoading = true))
    val uiState: StateFlow<SelectCategoryUiState> = _uiState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase() // Memanggil use case yang mengembalikan Flow<List<CategoryItem>>
                .map { allCategories ->
                    // Lakukan filter di sini, sebelum di-collect
                    allCategories.filter { it.type == transactionTypeToDisplay }
                }
                .onStart {
                    // Saat flow mulai di-collect, set state ke loading
                    _uiState.value = SelectCategoryUiState(isLoading = true)
                }
                .catch { exception ->
                    // Jika terjadi error saat mengambil data
                    _uiState.value =
                        SelectCategoryUiState(isLoading = false, error = exception.message)
                }
                .collect { filteredCategories ->
                    // Setelah data diterima dan difilter, update state dengan data baru
                    _uiState.value = SelectCategoryUiState(
                        isLoading = false,
                        categories = filteredCategories
                    )
                }
        }
    }

    companion object {
        const val ARG_TRANSACTION_TYPE = "transactionType"
    }
}

