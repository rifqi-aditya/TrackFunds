package com.rifqi.trackfunds.feature.categories.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCategoryViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ambil tipe transaksi dari argumen navigasi (SavedStateHandle)
    // Untuk sekarang, kita bisa set default atau buat use case yang lebih spesifik
    private val transactionTypeToDisplay: String =
        TransactionType.valueOf(
            savedStateHandle.get<String>(ARG_TRANSACTION_TYPE) ?: TransactionType.EXPENSE.name
        ).toString()

    private val _allCategoriesFromUseCase = MutableStateFlow<List<CategoryItem>>(emptyList())

    // categoriesToDisplay sekarang HANYA menampilkan kategori berdasarkan transactionTypeToDisplay
    // yang didapat saat ViewModel diinisialisasi.
    val categoriesToDisplay: StateFlow<List<CategoryItem>> =
        _allCategoriesFromUseCase.map { domainCategories ->
            domainCategories
                .filter { it.type == transactionTypeToDisplay } // Filter berdasarkan tipe yang di-pass saat navigasi
                .map { domainItem ->
                    CategoryItem(
                        id = domainItem.id,
                        name = domainItem.name,
                        iconIdentifier = domainItem.iconIdentifier,
                        type = domainItem.type // Simpan tipe untuk CategoryDisplayItem
                    )
                }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            // Asumsi GetCategoriesUseCase() mengembalikan SEMUA kategori
            // dan kita filter berdasarkan transactionTypeToDisplay di atas.
            // Jika GetCategoriesUseCase dimodifikasi untuk menerima tipe, panggilannya akan:
            // getCategoriesUseCase(transactionTypeToDisplay).collect { categories -> ... }
            getCategoriesUseCase().collect { categories ->
                _allCategoriesFromUseCase.value = categories
            }
        }
    }

    companion object {
        const val ARG_TRANSACTION_TYPE = "transactionType"
    }
}