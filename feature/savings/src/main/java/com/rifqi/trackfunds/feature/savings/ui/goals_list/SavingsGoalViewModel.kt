package com.rifqi.trackfunds.feature.savings.ui.goals_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.ui.utils.formatCurrency
import com.rifqi.trackfunds.feature.savings.ui.goals_list.SavingsGoalEvent
import com.rifqi.trackfunds.feature.savings.ui.model.toUiModel
import com.rifqi.trackfunds.feature.savings.ui.goals_list.SavingsGoalSideEffect
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
import javax.inject.Inject

@HiltViewModel
class SavingsGoalViewModel @Inject constructor(
    private val getActiveSavingsGoalsUseCase: GetActiveSavingsGoalsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavingsGoalUiState())
    val uiState: StateFlow<SavingsGoalUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<SavingsGoalSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        loadSavingsGoals()
    }

    private fun loadSavingsGoals() {
        viewModelScope.launch {
            getActiveSavingsGoalsUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { goals ->
                    val totalSaved = goals.sumOf { it.savedAmount }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            savingsGoals = goals.map { goals -> goals.toUiModel() },
                            totalSaved = formatCurrency(totalSaved)
                        )
                    }
                }
        }
    }

    fun onEvent(event: SavingsGoalEvent) {
        viewModelScope.launch {
            when (event) {
                is SavingsGoalEvent.AddGoalClicked -> {
                    viewModelScope.launch {
                        _sideEffect.emit(SavingsGoalSideEffect.NavigateToAddGoal)
                    }
                }

                is SavingsGoalEvent.GoalClicked -> {
                    viewModelScope.launch {
                        _sideEffect.emit(SavingsGoalSideEffect.NavigateToGoalDetails(event.goalId))
                    }
                }

                SavingsGoalEvent.NavigateBackClicked -> {
                    viewModelScope.launch {
                        _sideEffect.emit(SavingsGoalSideEffect.NavigateBack)
                    }
                }
            }
        }
    }
}