package com.rifqi.trackfunds.feature.savings.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.savings.model.GoalDetails
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.savings.usecase.DeleteSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.GetSavingsGoalDetailsUseCase
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.core.ui.utils.formatLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class GoalDetailViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSavingsGoalDetailsUseCase: GetSavingsGoalDetailsUseCase,
    private val deleteSavingsGoalUseCase: DeleteSavingsGoalUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoalDetailUiState())
    val uiState: StateFlow<GoalDetailUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<GoalDetailSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    private val goalId: String = savedStateHandle.get<String>("goalId") ?: ""

    init {
        loadGoalDetails()
    }

    fun onEvent(event: GoalDetailEvent) {
        when (event) {
            is GoalDetailEvent.OnNavigateBack -> sendSideEffect(GoalDetailSideEffect.NavigateBack)
            is GoalDetailEvent.OnEditClicked -> sendSideEffect(
                GoalDetailSideEffect.NavigateToEditGoal(
                    goalId
                )
            )

            is GoalDetailEvent.OnDeleteClicked -> _uiState.update { it.copy(showDeleteConfirmDialog = true) }
            is GoalDetailEvent.OnDismissDeleteDialog -> _uiState.update {
                it.copy(
                    showDeleteConfirmDialog = false
                )
            }

            is GoalDetailEvent.OnConfirmDelete -> deleteGoal()
            is GoalDetailEvent.OnAddFundsClicked -> sendSideEffect(
                GoalDetailSideEffect.ShowSnackbar(
                    "Add Funds feature coming soon!"
                )
            )
        }
    }

    private fun loadGoalDetails() {
        if (goalId.isEmpty()) {
            _uiState.update { it.copy(isLoading = false) }
            sendSideEffect(GoalDetailSideEffect.ShowSnackbar("Error: Goal ID not found."))
            return
        }

        viewModelScope.launch {
            getSavingsGoalDetailsUseCase(goalId)
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false) }
                    sendSideEffect(GoalDetailSideEffect.ShowSnackbar("Error: ${error.message}"))
                }
                .collect { goalDetails ->
                    if (goalDetails == null) {
                        _uiState.update { it.copy(isLoading = false) }
                        sendSideEffect(GoalDetailSideEffect.ShowSnackbar("Goal not found."))
                        return@collect
                    }
                    _uiState.update { mapToUiState(goalDetails) }
                }
        }
    }

    private fun deleteGoal() {
        viewModelScope.launch {
            // Tutup dialog dan tunjukkan loading state kecil jika perlu
            _uiState.update { it.copy(showDeleteConfirmDialog = false, isLoading = true) }

            val result = deleteSavingsGoalUseCase(goalId)

            result.onSuccess {
                sendSideEffect(GoalDetailSideEffect.ShowSnackbar("Goal deleted successfully"))
                sendSideEffect(GoalDetailSideEffect.NavigateBack)
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
                sendSideEffect(GoalDetailSideEffect.ShowSnackbar("Failed to delete: ${error.message}"))
            }
        }
    }

    private fun sendSideEffect(effect: GoalDetailSideEffect) {
        viewModelScope.launch {
            _sideEffect.emit(effect)
        }
    }

    /**
     * Mapper untuk mengubah hasil dari Use Case (GoalDetails) menjadi State untuk UI.
     */
    private fun mapToUiState(details: GoalDetails): GoalDetailUiState {
        return GoalDetailUiState(
            isLoading = false,
            goalName = details.goal.name,
            iconIdentifier = details.goal.iconIdentifier,
            progress = details.goal.progress,
            savedAmountFormatted = formatCurrency(details.goal.savedAmount), // Konversi BigDecimal -> String
            targetAmountFormatted = formatCurrency(details.goal.targetAmount), // Konversi BigDecimal -> String
            stats = listOf(
                StatItem("Avg. Saving/mo", formatCurrency(details.averageMonthlySaving)),
                StatItem("Est. Completion", details.estimatedCompletion)
            ),
            transactions = details.transactions.map { it.toTransactionUiModel() } // Map setiap item transaksi
        )
    }

    // Anda juga perlu mapper kecil untuk transaksi
    private fun Transaction.toTransactionUiModel(): GoalTransactionUiModel {
        return GoalTransactionUiModel(
            id = this.id,
            description = this.description,
            dateFormatted = formatLocalDateTime(this.date),
            amountFormatted = formatCurrency(this.amount.abs()),
            isIncome = this.amount > BigDecimal.ZERO
        )
    }

}