package com.rifqi.trackfunds.feature.savings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.SavingsGoalModel
import com.rifqi.trackfunds.core.domain.usecase.savings.CreateSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.validator.savings.ValidateIcon
import com.rifqi.trackfunds.core.domain.validator.savings.ValidateSavingsGoalName
import com.rifqi.trackfunds.core.domain.validator.savings.ValidateSavingsTargetAmount
import com.rifqi.trackfunds.core.navigation.api.AppScreen
import com.rifqi.trackfunds.core.navigation.api.SavingsRoutes
import com.rifqi.trackfunds.feature.savings.ui.event.AddEditSavingsEvent
import com.rifqi.trackfunds.feature.savings.ui.state.AddEditSavingsGoalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditSavingsGoalViewModel @Inject constructor(
    private val validateIcon: ValidateIcon,
    private val validateSavingsGoalName: ValidateSavingsGoalName,
    private val validateSavingsTargetAmount: ValidateSavingsTargetAmount,
    private val createSavingsGoalUseCase: CreateSavingsGoalUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSavingsGoalState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<AppScreen>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEvent(event: AddEditSavingsEvent) {
        when (event) {
            AddEditSavingsEvent.ShowIconPickerClicked ->
                _uiState.update { it.copy(showIconPicker = true) }

            AddEditSavingsEvent.IconPickerDismissed ->
                _uiState.update { it.copy(showIconPicker = false) }

            is AddEditSavingsEvent.IconIdentifierChanged ->
                _uiState.update {
                    it.copy(
                        iconIdentifier = event.identifier,
                        showIconPicker = false
                    )
                }

            is AddEditSavingsEvent.NameChanged ->
                _uiState.update { it.copy(goalName = event.name) }

            is AddEditSavingsEvent.TargetAmountChanged ->
                _uiState.update { it.copy(targetAmount = event.amount) }

            AddEditSavingsEvent.DateSelectorClicked -> _uiState.update { it.copy(showDatePicker = true) }
            AddEditSavingsEvent.DatePickerDismissed -> _uiState.update { it.copy(showDatePicker = false) }
            is AddEditSavingsEvent.DateSelected -> {
                // event.date adalah LocalDate
                _uiState.update {
                    it.copy(
                        targetDate = event.date,
                        showDatePicker = false
                    )
                }
            }

            AddEditSavingsEvent.SaveClicked -> saveGoal()
        }
    }

    private fun saveGoal() {

        val nameResult = validateSavingsGoalName(_uiState.value.goalName)
        val amountResult = validateSavingsTargetAmount(_uiState.value.targetAmount)
        val iconResult = validateIcon(_uiState.value.iconIdentifier)

        val hasError = listOf(nameResult, amountResult, iconResult).any { !it.isSuccess }

        _uiState.update {
            it.copy(
                goalNameError = nameResult.errorMessage,
                targetAmountError = amountResult.errorMessage,
                iconError = iconResult.errorMessage
            )
        }

        if (hasError) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    goalNameError = null,
                    targetAmountError = null,
                    iconError = null
                )
            }

            val currentState = _uiState.value
            val targetAmount = currentState.targetAmount.toBigDecimalOrNull() ?: BigDecimal.ZERO

            val newGoal = SavingsGoalModel(
                id = UUID.randomUUID()
                    .toString(),
                name = currentState.goalName,
                targetAmount = targetAmount,
                currentAmount = BigDecimal.ZERO,
                targetDate = currentState.targetDate?.atTime(LocalTime.MAX),
                iconIdentifier = currentState.iconIdentifier,
                isAchieved = false
            )

            createSavingsGoalUseCase(newGoal)
                .onSuccess {
                    _navigationEvent.emit(SavingsRoutes.Savings)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message) }
                }

            _uiState.update { it.copy(isLoading = false) }
        }
    }
}