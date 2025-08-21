package com.rifqi.trackfunds.feature.categories.ui.addedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.category.usecase.AddCategoryUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.DeleteCategoryUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.UpdateCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditCategoryViewModel @Inject constructor(
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val getCategoryUseCase: GetCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase, // <-- tambahkan use case delete
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditCategoryUiState())
    val uiState: StateFlow<AddEditCategoryUiState> = _uiState.asStateFlow()

    // Side effects (one-off)
    private val _effect = MutableSharedFlow<AddEditCategoryEffect>()
    val effect = _effect.asSharedFlow()

    private val argCategoryId: String? = savedStateHandle["categoryId"]
    private val argType: TransactionType =
        savedStateHandle.get<TransactionType>("type") ?: TransactionType.EXPENSE

    init {
        if (argCategoryId != null) {
            loadCategoryForEditing(argCategoryId)
        } else {
            prepareForAdding(argType)
        }
    }

    // --- Event handler ---

    fun onEvent(event: AddEditCategoryEvent) {
        when (event) {
            is AddEditCategoryEvent.NameChanged -> reduce { copy(name = event.name) }
            is AddEditCategoryEvent.IconChanged -> reduce { copy(iconIdentifier = event.iconIdentifier) }
            is AddEditCategoryEvent.TypeChanged -> reduce { copy(type = event.type) }

            AddEditCategoryEvent.IconPickerOpened -> reduce { copy(isIconSheetVisible = true) }
            AddEditCategoryEvent.IconPickerDismissed -> reduce { copy(isIconSheetVisible = false) }

            AddEditCategoryEvent.SaveClicked -> saveCategory()
            AddEditCategoryEvent.DeleteClicked -> deleteCategory()
        }
    }

    // --- Load / Prepare ---

    private fun loadCategoryForEditing(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getCategoryUseCase(id)
                .onSuccess { category ->
                    _uiState.update {
                        validate(
                            it.copy(
                                id = category.id,
                                name = category.name,
                                iconIdentifier = category.iconIdentifier,
                                type = category.type,
                                isLoading = false,
                                canDelete = true,
                                screenTitle = "Edit Category"
                            )
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = e.message ?: "Failed to load")
                    }
                    _effect.emit(AddEditCategoryEffect.ShowMessage("Failed to load category"))
                }
        }
    }

    private fun prepareForAdding(type: TransactionType) {
        _uiState.update {
            validate(
                it.copy(
                    id = null,
                    type = type,
                    canDelete = false,
                    screenTitle = if (type == TransactionType.EXPENSE)
                        "Add Expense Category" else "Add Income Category"
                )
            )
        }
    }

    // --- Actions ---

    private fun saveCategory() {
        val state = _uiState.value
        // hard validation di VM biar aman (selain UI canSubmit)
        val nameError = if (state.name.isBlank()) "Category name is required" else null
        if (nameError != null) {
            _uiState.update { it.copy(nameError = nameError, canSubmit = false) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val category = Category(
                id = state.id ?: UUID.randomUUID().toString(),
                name = state.name.trim(),
                iconIdentifier = state.iconIdentifier.ifBlank { "help" },
                type = state.type
            )

            val result = if (state.id != null) {
                updateCategoryUseCase(category)
            } else {
                addCategoryUseCase(category)
            }

            result
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    _effect.emit(AddEditCategoryEffect.Saved)
                }
                .onFailure { e ->
                    _uiState.update {
                        validate(it.copy(isSaving = false, errorMessage = e.message))
                    }
                    _effect.emit(
                        AddEditCategoryEffect.ShowMessage(
                            e.message ?: "Failed to save category"
                        )
                    )
                }
        }
    }

    private fun deleteCategory() {
        val id = _uiState.value.id ?: return // tidak bisa hapus saat add
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            deleteCategoryUseCase(id)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false) }
                    _effect.emit(AddEditCategoryEffect.Deleted)
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false, errorMessage = e.message) }
                    _effect.emit(
                        AddEditCategoryEffect.ShowMessage(
                            e.message ?: "Failed to delete category"
                        )
                    )
                }
        }
    }

    // --- Reducer & Validation ---

    private inline fun reduce(block: AddEditCategoryUiState.() -> AddEditCategoryUiState) {
        _uiState.update { validate(it.block()) }
    }

    private fun validate(s: AddEditCategoryUiState): AddEditCategoryUiState {
        val nameErr = if (s.name.isBlank()) "Category name is required" else null
        val canSubmit = nameErr == null && !s.isSaving
        return s.copy(nameError = nameErr, canSubmit = canSubmit)
    }
}
