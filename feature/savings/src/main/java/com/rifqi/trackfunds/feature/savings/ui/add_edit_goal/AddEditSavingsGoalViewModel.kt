package com.rifqi.trackfunds.feature.savings.ui.add_edit_goal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.usecase.savings.CreateSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetSavingsGoalByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.UpdateSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.validator.savings.ValidateIcon
import com.rifqi.trackfunds.core.domain.validator.savings.ValidateSavingsGoalName
import com.rifqi.trackfunds.core.domain.validator.savings.ValidateSavingsTargetAmount
import com.rifqi.trackfunds.feature.savings.ui.model.toDomainModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditSavingsGoalViewModel @Inject constructor(
    private val validateIcon: ValidateIcon,
    private val validateSavingsGoalName: ValidateSavingsGoalName,
    private val validateSavingsTargetAmount: ValidateSavingsTargetAmount,
    private val getSavingsGoalByIdUseCase: GetSavingsGoalByIdUseCase,
    private val createSavingsGoalUseCase: CreateSavingsGoalUseCase,
    private val updateSavingsGoalUseCase: UpdateSavingsGoalUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSavingsGoalState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<AddEditGoalSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        // Ambil goalId dari argumen navigasi. "goalId" harus cocok dengan nama argumen di NavGraph Anda.
        val goalId: String? = savedStateHandle["goalId"]

        if (goalId != null) {
            // Jika goalId ada, kita masuk ke MODE EDIT
            loadGoalForEditing(goalId)
        } else {
            // Jika goalId null, kita masuk ke MODE TAMBAH BARU
            // (State default sudah kosong, jadi tidak perlu melakukan apa-apa)
        }
    }

    private fun loadGoalForEditing(goalId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Panggil use case untuk mendapatkan data goal
            getSavingsGoalByIdUseCase(goalId).firstOrNull()?.let { goal ->
                // Update UI State dengan data yang sudah ada
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEditing = true,
                        editingGoalId = goal.id, // Simpan ID untuk proses update
                        pageTitle = "Edit Savings Goal",
                        goalName = goal.name,
                        targetAmount = goal.targetAmount.toPlainString(),
                        targetDate = goal.targetDate,
                        iconIdentifier = goal.iconIdentifier,
                        originalSavedAmount = goal.savedAmount
                    )
                }
            }
        }
    }


    fun onEvent(event: AddEditSavingsEvent) {
        when (event) {
            AddEditSavingsEvent.ShowIconPickerClicked -> handleShowIconPickerClicked()
            AddEditSavingsEvent.IconPickerDismissed -> handleIconPickerDismissed()
            is AddEditSavingsEvent.IconIdentifierChanged -> handleIconIdentifierChanged(event)
            is AddEditSavingsEvent.NameChanged -> handleNameChanged(event)
            is AddEditSavingsEvent.TargetAmountChanged -> handleTargetAmountChanged(event)
            AddEditSavingsEvent.DateSelectorClicked -> handleDateSelectorClicked()
            AddEditSavingsEvent.DatePickerDismissed -> handleDatePickerDismissed()
            is AddEditSavingsEvent.DateSelected -> handleDateSelected(event)
            AddEditSavingsEvent.SaveClicked -> saveGoal()
        }
    }

    private fun handleShowIconPickerClicked() {
        _uiState.update { it.copy(showIconPicker = true) }
    }

    private fun handleIconPickerDismissed() {
        _uiState.update { it.copy(showIconPicker = false) }
    }

    private fun handleIconIdentifierChanged(event: AddEditSavingsEvent.IconIdentifierChanged) {
        _uiState.update {
            it.copy(
                iconIdentifier = event.identifier,
                showIconPicker = false
            )
        }
    }

    private fun handleNameChanged(event: AddEditSavingsEvent.NameChanged) {
        _uiState.update { it.copy(goalName = event.name) }
    }

    private fun handleTargetAmountChanged(event: AddEditSavingsEvent.TargetAmountChanged) {
        _uiState.update { it.copy(targetAmount = event.amount) }
    }

    private fun handleDateSelectorClicked() {
        _uiState.update { it.copy(showDatePicker = true) }
    }

    private fun handleDatePickerDismissed() {
        _uiState.update { it.copy(showDatePicker = false) }
    }

    private fun handleDateSelected(event: AddEditSavingsEvent.DateSelected) {
        _uiState.update {
            it.copy(
                targetDate = event.date,
                showDatePicker = false
            )
        }
    }

    private fun saveGoal() {
        // Ambil state saat ini sekali saja untuk divalidasi
        val currentState = _uiState.value

        val nameResult = validateSavingsGoalName(currentState.goalName)
        val amountResult = validateSavingsTargetAmount(currentState.targetAmount)
        val iconResult = validateIcon(currentState.iconIdentifier)

        val hasError = listOf(nameResult, amountResult, iconResult).any { !it.isSuccess }

        // Update semua error message ke UI dalam satu kali panggilan
        _uiState.update {
            it.copy(
                goalNameError = nameResult.errorMessage,
                targetAmountError = amountResult.errorMessage,
                iconError = iconResult.errorMessage
            )
        }

        // Jika ada error, hentikan proses
        if (hasError) {
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        goalNameError = null,
                        targetAmountError = null,
                        iconError = null
                    )
                }

                val result = if (currentState.isEditing) {
                    updateExistingGoal(currentState)
                } else {
                    createNewGoal(currentState)
                }

                result.onSuccess {
                    _sideEffect.emit(AddEditGoalSideEffect.ShowSnackbar("Goal saved successfully!"))
                    _sideEffect.emit(AddEditGoalSideEffect.NavigateBack)
                }.onFailure { error ->
                    _sideEffect.emit(AddEditGoalSideEffect.ShowSnackbar("Error: ${error.message}"))
                }

            } finally {
                // Blok finally akan SELALU dieksekusi, memastikan isLoading kembali false
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Helper function untuk membuat goal baru
    private suspend fun createNewGoal(state: AddEditSavingsGoalState): Result<Unit> {
        val domainModelResult = state.toDomainModel()

        return domainModelResult.fold(
            onSuccess = { newGoal -> createSavingsGoalUseCase(newGoal) },
            onFailure = { error -> Result.failure(error) }
        )
    }

    // Helper function untuk mengupdate goal yang ada
    private suspend fun updateExistingGoal(state: AddEditSavingsGoalState): Result<Unit> {
        // Anda perlu mapper yang bisa menerima ID yang sudah ada
        val domainModelResult = state.toDomainModel(id = state.editingGoalId!!)

        return domainModelResult.fold(
            onSuccess = { updatedGoal -> updateSavingsGoalUseCase(updatedGoal) },
            onFailure = { error -> Result.failure(error) }
        )
    }
}