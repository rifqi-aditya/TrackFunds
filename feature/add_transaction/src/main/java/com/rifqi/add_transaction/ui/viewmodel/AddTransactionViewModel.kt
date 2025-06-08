package com.rifqi.add_transaction.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.add_transaction.ui.model.AccountDisplayItem
import com.rifqi.add_transaction.ui.model.AddTransactionUiState
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

// Definisikan konstanta kunci SavedStateHandle di tempat yang bisa diakses,
// idealnya di modul navigasi atau common. Untuk contoh ini, saya akan asumsikan
// sudah ada di com.rifqi.trackfunds.core.navigation

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    // TODO: Inject UseCases yang relevan saat sudah dibuat
    // private val addTransactionUseCase: AddTransactionUseCase,
    // private val getAccountsUseCase: GetAccountsUseCase,
    private val savedStateHandle: SavedStateHandle // Untuk mengambil argumen navigasi atau hasil
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    // Contoh data dummy untuk daftar akun (nantinya dari GetAccountsUseCase)
    private val dummyAvailableAccounts = listOf(
        AccountDisplayItem(
            id = "acc1",
            name = "Cash Wallet",
            iconIdentifier = "ic_wallet_placeholder"
        ),
        AccountDisplayItem(id = "acc2", name = "Mbanking BCA", iconIdentifier = "ic_bank_bca"),
        AccountDisplayItem(id = "acc3", name = "Gopay", iconIdentifier = "ic_gopay")
    )

    // State untuk daftar akun yang bisa dipilih (bisa juga di-load di init)
    val availableAccounts: List<AccountDisplayItem> = dummyAvailableAccounts // Nanti dari use case


    init {
        // Ambil data yang mungkin dikembalikan dari layar lain
        // Misal, setelah memilih kategori
        savedStateHandle.get<CategoryItem>(SELECTED_CATEGORY_KEY)?.let { category ->
            onCategorySelected(category)
            savedStateHandle.remove<CategoryItem>(SELECTED_CATEGORY_KEY) // Hapus agar tidak diproses ulang
        }
        // Misal, setelah mengisi catatan
        savedStateHandle.get<String>(SAVED_NOTE_KEY)?.let { note ->
            onNotesChange(note) // Langsung update state notes
            savedStateHandle.remove<String>(SAVED_NOTE_KEY)
        }
        // Misal, setelah memilih akun
        savedStateHandle.get<AccountDisplayItem>(SELECTED_ACCOUNT_KEY)?.let { account ->
            onAccountSelected(account)
            savedStateHandle.remove<AccountDisplayItem>(SELECTED_ACCOUNT_KEY)
        }

        // loadInitialData() // Panggil jika ada data awal yang perlu di-load seperti daftar akun
    }

    // private fun loadInitialData() {
    //     viewModelScope.launch {
    //         // _uiState.update { it.copy(isLoadingAccounts = true) }
    //         // val accounts = getAccountsUseCase().firstOrNull() ?: emptyList()
    //         // _uiState.update { it.copy(isLoadingAccounts = false, availableAccounts = accounts.mapToDisplayItem()) }
    //     }
    // }

    fun onAmountChange(amount: String) {
        // Hanya izinkan angka dan mungkin satu titik desimal jika diperlukan
        // Untuk Rupiah, biasanya tidak ada desimal di input, tapi visual transformation bisa menanganinya
        val newAmount = amount.filter { it.isDigit() }
        _uiState.update { it.copy(amount = newAmount) }
    }

    fun onTransactionTypeSelected(type: TransactionType) {
        _uiState.update {
            it.copy(
                selectedTransactionType = type,
                selectedCategory = null
            )
        } // Reset kategori saat tipe berubah
    }

    fun onAccountSelected(account: AccountDisplayItem) {
        _uiState.update { it.copy(selectedAccount = account) }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onCategorySelected(category: CategoryItem?) { // Dipanggil saat kembali dari SelectCategoryScreen
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun saveTransaction() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val currentState = _uiState.value
            // Validasi input sederhana
            if (currentState.amount.isBlank() || currentState.amount.toDoubleOrNull() == 0.0) {
                _uiState.update {
                    it.copy(
                        error = "Jumlah tidak boleh kosong atau nol.",
                        isLoading = false
                    )
                }
                return@launch
            }
            if (currentState.selectedAccount == null) {
                _uiState.update { it.copy(error = "Harap pilih akun.", isLoading = false) }
                return@launch
            }
            if (currentState.selectedCategory == null) {
                _uiState.update { it.copy(error = "Harap pilih kategori.", isLoading = false) }
                return@launch
            }

            // TODO: Buat objek TransactionDomainModel dari uiState.value
            // val transactionToSave = TransactionDomainModel(
            //     amount = currentState.amount.toDouble(),
            //     type = currentState.selectedTransactionType,
            //     accountId = currentState.selectedAccount.id,
            //     date = currentState.selectedDate,
            //     time = currentState.selectedTime,
            //     notes = currentState.notes,
            //     categoryId = currentState.selectedCategory.id
            // )

            // TODO: Panggil addTransactionUseCase
            // val result = addTransactionUseCase(transactionToSave)
            // if (result.isSuccess) {
            //     _uiState.update { it.copy(isLoading = false, transactionSaved = true) }
            // } else {
            //     _uiState.update { it.copy(isLoading = false, error = result.exceptionOrNull()?.message ?: "Gagal menyimpan transaksi") }
            // }

            // Simulasi sukses untuk sekarang
            kotlinx.coroutines.delay(1000)
            println("Simpan Transaksi: ${currentState.amount}, Tipe: ${currentState.selectedTransactionType}, Akun: ${currentState.selectedAccount?.name}, Kategori: ${currentState.selectedCategory?.name}, Catatan: ${currentState.notes}, Tanggal: ${currentState.selectedDate}")
            _uiState.update { it.copy(isLoading = false, transactionSaved = true) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetTransactionSavedFlag() {
        _uiState.update { it.copy(transactionSaved = false) }
    }

    companion object {
        const val SELECTED_CATEGORY_KEY = "selected_category"
        const val SAVED_NOTE_KEY = "saved_note"
        const val SELECTED_ACCOUNT_KEY = "selected_account"
    }
}