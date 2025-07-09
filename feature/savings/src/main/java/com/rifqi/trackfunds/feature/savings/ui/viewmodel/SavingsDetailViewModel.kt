package com.rifqi.trackfunds.feature.savings.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.usecase.savings.DeleteSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetSavingsGoalByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.navigation.api.AddEditSavingsGoal
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.feature.savings.ui.event.SavingsDetailEvent
import com.rifqi.trackfunds.feature.savings.ui.state.SavingsDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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
class SavingsDetailViewModel @Inject constructor(
    private val getSavingsGoalByIdUseCase: GetSavingsGoalByIdUseCase,
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
    private val deleteSavingsGoalUseCase: DeleteSavingsGoalUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val goalId: String = savedStateHandle.toRoute<AddEditSavingsGoal>().goalId!!

    private val _uiState = MutableStateFlow(SavingsDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen?>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            // Buat filter khusus untuk mengambil transaksi yang terhubung dengan goalId ini
            val transactionFilter = TransactionFilter(savingsGoalId = goalId)

            combine(
                getSavingsGoalByIdUseCase(goalId),
                getFilteredTransactionsUseCase(transactionFilter)
            ) { goal, transactions ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        goal = goal,
                        history = transactions
                    )
                }
            }
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect()
        }
    }

    fun onEvent(event: SavingsDetailEvent) {
        viewModelScope.launch {
            when (event) {
                SavingsDetailEvent.EditGoalClicked -> {
                    _navigationEvent.emit(AddEditSavingsGoal(goalId = goalId))
                }

                SavingsDetailEvent.AddFundsClicked -> {
                    // TODO: Navigasi ke AddEditTransaction dengan mode Savings
                }

                SavingsDetailEvent.DeleteGoalClicked -> {
                    _uiState.update { it.copy(showDeleteConfirmDialog = true) }
                }

                SavingsDetailEvent.DismissDeleteDialog -> {
                    _uiState.update { it.copy(showDeleteConfirmDialog = false) }
                }

                SavingsDetailEvent.ConfirmDeleteClicked -> {
                    deleteGoal()
                }
            }
        }
    }

    private suspend fun deleteGoal() {
        _uiState.update { it.copy(isLoading = true, showDeleteConfirmDialog = false) }
        try {
            deleteSavingsGoalUseCase(goalId)
            _navigationEvent.emit(null)
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }
}