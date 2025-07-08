package com.rifqi.trackfunds.feature.savings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.SavingsGoalItem
import com.rifqi.trackfunds.core.domain.usecase.savings.CreateSavingsGoalUseCase
import com.rifqi.trackfunds.feature.savings.ui.event.AddEditSavingsEvent
import com.rifqi.trackfunds.feature.savings.ui.state.AddEditSavingsGoalState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditSavingsGoalViewModel @Inject constructor(
    private val createSavingsGoalUseCase: CreateSavingsGoalUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSavingsGoalState())
    val uiState = _uiState.asStateFlow()

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
        val name = _uiState.value.goalName
        val amount = _uiState.value.targetAmount.toBigDecimalOrNull()
        val iconIdentifier = _uiState.value.iconIdentifier

        val nameError = if (name.isBlank()) "Savings name must be filled in" else null
        val amountError =
            if (amount == null || amount < BigDecimal("10000")) "Target of at least IDR 10,000" else null
        val iconError = if (iconIdentifier.isBlank()) "Please choose an icon first" else null

        _uiState.update {
            it.copy(
                goalNameError = nameError,
                targetAmountError = amountError,
                iconError = iconError
            )
        }

        if (nameError == null && amountError == null && iconError == null) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                val newGoal = SavingsGoalItem(
                    id = UUID.randomUUID().toString(),
                    name = _uiState.value.goalName,
                    targetAmount = _uiState.value.targetAmount.toBigDecimalOrNull()
                        ?: BigDecimal.ZERO,
                    currentAmount = BigDecimal.ZERO,
                    targetDate = _uiState.value.targetDate?.atTime(LocalTime.MAX),
                    iconIdentifier = _uiState.value.iconIdentifier,
                    isAchieved = false
                )

                createSavingsGoalUseCase(newGoal)
                _uiState.update { it.copy(isLoading = false, isGoalSaved = true) }
            }
        }
    }
}