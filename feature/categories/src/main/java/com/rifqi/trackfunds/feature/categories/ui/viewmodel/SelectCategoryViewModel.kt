package com.rifqi.trackfunds.feature.categories.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import com.rifqi.trackfunds.core.domain.category.usecase.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.feature.categories.ui.state.SelectCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val getFilteredCategoriesUseCase: GetFilteredCategoriesUseCase,
    savedStateHandle: SavedStateHandle,
    private val resultManager: NavigationResultManager
) : ViewModel() {

    private val transactionTypeToDisplay: TransactionType = try {
        TransactionType.valueOf(
            savedStateHandle.get<String>("transactionType") ?: TransactionType.EXPENSE.name
        )
    } catch (e: IllegalArgumentException) {
        TransactionType.EXPENSE
    }

    private val _uiState = MutableStateFlow(SelectCategoryUiState(isLoading = true))
    val uiState: StateFlow<SelectCategoryUiState> = _uiState

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getFilteredCategoriesUseCase(filter = CategoryFilter())
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

    fun onCategorySelected(category: Category) {
        resultManager.setResult(category)
    }
}

