package com.rifqi.trackfunds.feature.categories.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.SharedRoutes
import com.rifqi.trackfunds.feature.categories.ui.event.CategoryListEvent
import com.rifqi.trackfunds.feature.categories.ui.state.CategoryListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
    private val getFilteredCategoriesUseCase: GetFilteredCategoriesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryListUiState())
    val uiState: StateFlow<CategoryListUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            val expenseFilter = CategoryFilter(type = TransactionType.EXPENSE)
            val incomeFilter = CategoryFilter(type = TransactionType.INCOME)

            combine(
                getFilteredCategoriesUseCase(expenseFilter),
                getFilteredCategoriesUseCase(incomeFilter)
            ) { expenseList, incomeList ->
                val (defaultExpenses, userExpenses) = expenseList.partition { it.userUid == null }
                val (defaultIncomes, userIncomes) = incomeList.partition { it.userUid == null }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        defaultExpenseCategories = defaultExpenses,
                        userExpenseCategories = userExpenses,
                        defaultIncomeCategories = defaultIncomes,
                        userIncomeCategories = userIncomes
                    )
                }
            }
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect()
        }
    }

    fun onEvent(event: CategoryListEvent) {
        viewModelScope.launch {
            when (event) {
                is CategoryListEvent.CategoryClicked -> {
                    // Kirim sinyal untuk navigasi ke halaman edit dengan membawa ID
                    _navigationEvent.emit(SharedRoutes.AddEditCategory(categoryId = event.categoryId))
                }

                is CategoryListEvent.AddCategoryClicked -> {
                    // Kirim sinyal untuk navigasi ke halaman tambah dengan membawa TIPE
                    _navigationEvent.emit(SharedRoutes.AddEditCategory(type = event.type))
                }
            }
        }
    }
}