package com.rifqi.trackfunds.feature.scan.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.domain.model.ReceiptItemModel
import com.rifqi.trackfunds.core.domain.model.TransactionModel
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.model.filter.CategoryFilter
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.feature.scan.ui.event.ScanReceiptEvent
import com.rifqi.trackfunds.feature.scan.ui.sideeffect.ScanReceiptSideEffect
import com.rifqi.trackfunds.feature.scan.ui.state.ScanPhase
import com.rifqi.trackfunds.feature.scan.ui.state.ScanReceiptUiState
import com.rifqi.trackfunds.feature.scan.ui.state.ScanSheetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class ScanReceiptViewModel @Inject constructor(
    private val scanReceiptUseCase: ScanReceiptUseCase,
    private val getCategoryByStandardKeyUseCase: GetCategoryByStandardKeyUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getFilteredCategoriesUseCase: GetFilteredCategoriesUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val navigationResultManager: NavigationResultManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanReceiptUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<ScanReceiptSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val categoriesForSelection: StateFlow<List<CategoryModel>> =
        _uiState.map { TransactionType.EXPENSE to it.categorySearchQuery }
            .distinctUntilChanged()
            .flatMapLatest { (type, query) ->
                getFilteredCategoriesUseCase(CategoryFilter(type = type))
                    .map { categories ->
                        if (query.isBlank()) {
                            categories
                        } else {
                            categories.filter {
                                it.name.contains(query, ignoreCase = true)
                            }
                        }
                    }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    init {
        observeCameraResult()
        viewModelScope.launch {
            val allAccounts = getAccountsUseCase().first()
            _uiState.update {
                it.copy(
                    allAccounts = allAccounts,
                )
            }
        }
    }

    fun onEvent(event: ScanReceiptEvent) {
        viewModelScope.launch {
            when (event) {
                is ScanReceiptEvent.ImageSelected -> {
                    _uiState.update {
                        it.copy(
                            currentPhase = ScanPhase.IMAGE_PREVIEW,
                            imagePreviewUri = event.uri
                        )
                    }
                }

                is ScanReceiptEvent.ConfirmImage -> {
                    _uiState.value.imagePreviewUri?.let { uri ->
                        processImage(uri)
                    }
                }

                ScanReceiptEvent.ScanReceiptAgainClicked -> _uiState.update {
                    it.copy(currentPhase = ScanPhase.UPLOAD, scanResult = null, errorMessage = null)
                }

                ScanReceiptEvent.SelectFromCameraClicked -> {
                    _sideEffect.emit(ScanReceiptSideEffect.NavigateToCamera)
                }

                ScanReceiptEvent.SelectFromGalleryClicked -> {
                    _sideEffect.emit(ScanReceiptSideEffect.LaunchGallery)
                }

                is ScanReceiptEvent.DescriptionChanged -> _uiState.update {
                    it.copy(editableTransaction = it.editableTransaction?.copy(description = event.description))
                }

                is ScanReceiptEvent.AmountChanged -> _uiState.update {
                    it.copy(
                        editableTransaction = it.editableTransaction?.copy(
                            amount = event.amount.toBigDecimalOrNull() ?: BigDecimal.ZERO
                        )
                    )
                }

                ScanReceiptEvent.ToggleItemListExpansion -> _uiState.update {
                    it.copy(
                        isItemListExpanded = !it.isItemListExpanded
                    )
                }

                ScanReceiptEvent.ToggleReceiptImageExpansion -> _uiState.update {
                    it.copy(
                        isReceiptImageExpanded = !it.isReceiptImageExpanded
                    )
                }

                ScanReceiptEvent.SaveTransactionClicked -> {
                    saveTransaction()
                }

                is ScanReceiptEvent.AccountClicked -> {
                    _uiState.update { it.copy(activeSheet = ScanSheetType.ACCOUNT) }
                }

                is ScanReceiptEvent.AccountSelected -> {
                    _uiState.update {
                        it.copy(
                            editableTransaction = _uiState.value.editableTransaction?.copy(account = event.account),
                            activeSheet = null,
                        )
                    }
                }

                is ScanReceiptEvent.CategoryClicked -> {
                    _uiState.update { it.copy(activeSheet = ScanSheetType.CATEGORY) }
                }

                is ScanReceiptEvent.CategorySelected -> {
                    _uiState.update {
                        it.copy(
                            editableTransaction = _uiState.value.editableTransaction?.copy(category = event.category),
                            activeSheet = null,
                        )
                    }
                }

                is ScanReceiptEvent.DateClicked -> {
                    _uiState.update { it.copy(showDatePicker = true) }
                }

                is ScanReceiptEvent.DateChanged -> {
                    _uiState.update {
                        // Gabungkan tanggal baru dengan waktu yang sudah ada
                        val newDateTime = _uiState.value.editableTransaction?.date?.with(event.date)
                        it.copy(
                            editableTransaction = _uiState.value.editableTransaction?.copy(
                                date = newDateTime ?: event.date.atStartOfDay()
                            ),
                            showDatePicker = false
                        )
                    }
                }

                is ScanReceiptEvent.DismissAllPickers -> {
                    _uiState.update {
                        it.copy(
                            activeSheet = null,
                            showDatePicker = false
                        )
                    }
                }

                is ScanReceiptEvent.LineItemNameChanged -> {
                    updateLineItem(event.index) { it.copy(name = event.newName) }
                }

                is ScanReceiptEvent.LineItemQuantityChanged -> {
                    updateAndRecalculate(event.index) { it.copy(quantity = event.newQuantity) }
                }

                is ScanReceiptEvent.LineItemPriceChanged -> {
                    if (event.newPrice.all { it.isDigit() }) {
                        updateAndRecalculate(event.index) {
                            it.copy(
                                price = event.newPrice.toBigDecimalOrNull() ?: BigDecimal.ZERO
                            )
                        }
                    }
                }

                is ScanReceiptEvent.DeleteLineItem -> {
                    _uiState.value.editableTransaction?.let { transaction ->
                        val updatedItems = transaction.receiptItemModels.toMutableList().apply {
                            removeAt(event.index)
                        }

                        val newTotal = recalculateTotalFromItems(updatedItems)
                        _uiState.update {
                            it.copy(
                                editableTransaction = transaction.copy(
                                    receiptItemModels = updatedItems,
                                    amount = newTotal
                                )
                            )
                        }
                    }
                }

                ScanReceiptEvent.AddLineItem -> {
                    _uiState.value.editableTransaction?.let { transaction ->
                        val newItem = ReceiptItemModel("", 1, BigDecimal.ZERO)
                        val updatedItems = transaction.receiptItemModels + newItem
                        _uiState.update {
                            it.copy(
                                editableTransaction = transaction.copy(
                                    receiptItemModels = updatedItems
                                )
                            )
                        }
                    }
                }

                is ScanReceiptEvent.CategorySearchQueryChanged -> {
                    _uiState.update {
                        it.copy(categorySearchQuery = event.query)
                    }
                }
            }
        }
    }

    private fun observeCameraResult() {
        navigationResultManager.result
            .onEach { result ->
                if (result is Uri) {
                    onEvent(ScanReceiptEvent.ImageSelected(result))
                    navigationResultManager.setResult(null)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun processImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(currentPhase = ScanPhase.PROCESSING) }

            scanReceiptUseCase(uri)
                .onSuccess { result ->
                    // ---- Mulai Logika Baru di Sini ----

                    // 1. Ambil list semua akun pengguna
                    val allAccounts = getAccountsUseCase().first()

                    // 2. Tentukan akun default (misalnya, yang pertama dalam daftar)
                    val defaultAccount = allAccounts.firstOrNull()

                    // 3. Handle kasus jika pengguna belum punya akun sama sekali
                    if (defaultAccount == null) {
                        _uiState.update {
                            it.copy(
                                currentPhase = ScanPhase.UPLOAD, // Kembalikan ke awal
                                errorMessage = "You must create an account first."
                            )
                        }
                        return@onSuccess // Hentikan proses
                    }

                    // Cocokkan kategori (tidak berubah)
                    val suggestedCategory = result.categoryStandardKey?.let {
                        getCategoryByStandardKeyUseCase(it)
                    }

                    // 4. Buat objek TransactionModel awal dengan akun default
                    val initialTransaction = TransactionModel(
                        amount = result.totalAmount,
                        date = result.transactionDateTime,
                        description = result.merchantName ?: "",
                        category = suggestedCategory,
                        receiptItemModels = result.receiptItemModels,
                        account = defaultAccount,
                        type = TransactionType.EXPENSE
                    )


                    // 5. Update state ke fase REVIEW
                    _uiState.update {
                        it.copy(
                            currentPhase = ScanPhase.REVIEW,
                            scanResult = result,
                            editableTransaction = initialTransaction
                        )
                    }

                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(currentPhase = ScanPhase.UPLOAD, errorMessage = error.message)
                    }
                }
        }
    }

    private fun updateLineItem(index: Int, updater: (ReceiptItemModel) -> ReceiptItemModel) {
        _uiState.value.editableTransaction?.let { transaction ->
            val updatedItems = transaction.receiptItemModels.toMutableList().apply {
                val updatedItem = updater(this[index])
                this[index] = updatedItem
            }
            _uiState.update { it.copy(editableTransaction = transaction.copy(receiptItemModels = updatedItems)) }
        }
    }

    private fun recalculateTotalFromItems(items: List<ReceiptItemModel>): BigDecimal {
        return items.sumOf {
            it.price.multiply(it.quantity.toBigDecimal())
        }
    }

    private fun updateAndRecalculate(index: Int, updater: (ReceiptItemModel) -> ReceiptItemModel) {
        _uiState.value.editableTransaction?.let { transaction ->
            // 1. Buat list item yang sudah diperbarui
            val updatedItems = transaction.receiptItemModels.toMutableList().apply {
                this[index] = updater(this[index])
            }
            // 2. Hitung total baru dari list yang diperbarui
            val newTotal = recalculateTotalFromItems(updatedItems)

            // 3. Update state dengan list dan total yang baru
            _uiState.update {
                it.copy(
                    editableTransaction = transaction.copy(
                        receiptItemModels = updatedItems,
                        amount = newTotal
                    )
                )
            }
        }
    }

    private fun saveTransaction() {
        viewModelScope.launch {
            val transactionToSave = _uiState.value.editableTransaction

            if (transactionToSave == null) {
                _uiState.update { it.copy(errorMessage = "Transaction data is missing.") }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true) }

            // Panggil UseCase untuk menyimpan transaksi
            addTransactionUseCase(transactionToSave)
                .onSuccess {
                    _sideEffect.emit(ScanReceiptSideEffect.NavigateBack)
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
        }
    }
}