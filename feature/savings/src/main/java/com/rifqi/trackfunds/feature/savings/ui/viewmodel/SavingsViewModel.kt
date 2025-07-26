package com.rifqi.trackfunds.feature.savings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.SavingsRoutes
import com.rifqi.trackfunds.feature.savings.ui.event.SavingsEvent
import com.rifqi.trackfunds.feature.savings.ui.state.SavingsUiState
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
class SavingsViewModel @Inject constructor(
    private val getActiveSavingsGoalsUseCase: GetActiveSavingsGoalsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavingsUiState())
    val uiState: StateFlow<SavingsUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        loadSavingsGoals()
    }

    private fun loadSavingsGoals() {
        viewModelScope.launch {
            getActiveSavingsGoalsUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { goals ->
                    val totalSavings = goals.sumOf { it.currentAmount }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            savingsGoalModels = goals,
                            totalSavings = totalSavings
                        )
                    }
                }
        }
    }

    fun onEvent(event: SavingsEvent) {
        viewModelScope.launch {
            when (event) {
                SavingsEvent.AddNewGoalClicked -> {
                    _navigationEvent.emit(SavingsRoutes.AddEditSavingsGoal())
                }

                is SavingsEvent.GoalClicked -> {
                    _navigationEvent.emit(SavingsRoutes.SavingsDetail(goalId = event.goalId))
                }
            }
        }
    }
}