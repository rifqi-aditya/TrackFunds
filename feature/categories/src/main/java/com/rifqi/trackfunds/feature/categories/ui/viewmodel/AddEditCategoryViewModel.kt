package com.rifqi.trackfunds.feature.categories.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.category.AddCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.UpdateCategoryUseCase
import com.rifqi.trackfunds.feature.categories.ui.event.AddEditCategoryEvent
import com.rifqi.trackfunds.feature.categories.ui.state.AddEditCategoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditCategoryUiState())
    val uiState = _uiState.asStateFlow()

    // Ambil categoryId dari argumen navigasi
    private val categoryId: String? = savedStateHandle["categoryId"]

    init {
        if (categoryId != null) {
            loadCategoryForEditing(categoryId)
        } else {
            val type = savedStateHandle.get<TransactionType>("type") ?: TransactionType.EXPENSE
            prepareForAdding(type)
        }
    }

    private fun loadCategoryForEditing(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val category = getCategoryUseCase(id)
            if (category != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        name = category.name,
                        iconIdentifier = category.iconIdentifier,
                        type = category.type,
                        screenTitle = "Edit Kategori"
                    )
                }
            } else {
                // Handle error jika kategori tidak ditemukan
                _uiState.update { it.copy(isLoading = false, error = "Kategori tidak ditemukan.") }
            }
        }
    }

    private fun prepareForAdding(type: TransactionType) {
        _uiState.update {
            it.copy(
                type = type,
                screenTitle = if (type == TransactionType.EXPENSE) "Tambah Kategori Pengeluaran" else "Tambah Kategori Pemasukan"
            )
        }
    }

    fun onEvent(event: AddEditCategoryEvent) {
        when (event) {
            is AddEditCategoryEvent.NameChanged -> {
                _uiState.update { it.copy(name = event.name) }
            }

            is AddEditCategoryEvent.IconChanged -> {
                _uiState.update { it.copy(iconIdentifier = event.iconIdentifier) }
            }

            AddEditCategoryEvent.SaveClicked -> {
                saveCategory()
            }
        }
    }

    private fun saveCategory() {
        viewModelScope.launch {
            val currentState = _uiState.value
            // Validasi sederhana
            if (currentState.name.isBlank() || currentState.iconIdentifier.isBlank()) {
                _uiState.update { it.copy(error = "Nama dan ikon tidak boleh kosong.") }
                return@launch
            }

            val categoryToSave = CategoryItem(
                id = categoryId ?: UUID.randomUUID()
                    .toString(), // Gunakan ID yang ada atau buat baru
                name = currentState.name,
                iconIdentifier = currentState.iconIdentifier,
                type = currentState.type
            )

            if (categoryId != null) {
                updateCategoryUseCase(categoryToSave)
            } else {
                addCategoryUseCase(categoryToSave)
            }

            // Set flag untuk memberi tahu UI bahwa penyimpanan berhasil dan bisa navigasi kembali
            _uiState.update { it.copy(isCategorySaved = true) }
        }
    }
}