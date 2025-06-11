package com.rifqi.trackfunds.feature.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.ui.util.getCurrentDateRange
import com.rifqi.trackfunds.core.ui.util.getCurrentMonthAndYear
import com.rifqi.trackfunds.feature.home.ui.model.HomeSummary
import com.rifqi.trackfunds.feature.home.ui.model.HomeTransactionItem
import com.rifqi.trackfunds.feature.home.ui.model.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

val DUMMY_HOME_SUMMARY_DATA = HomeSummary(
    monthlyBalance = BigDecimal("1250000.0"),
    totalExpenses = BigDecimal("1250000.0"),
    totalIncome = BigDecimal("1250000.0"),
    recentExpenses = listOf(
        HomeTransactionItem("e1", "Groceries", "shopping_cart", BigDecimal("1250000.0"), "Expense"),
        HomeTransactionItem("e2", "Dining Out", "restaurant", BigDecimal("1250000.0"), "Expense")
    ),
    recentIncome = listOf(
        HomeTransactionItem("i1", "Salary", "cash", BigDecimal("1250000.0"), "Income"),
        HomeTransactionItem("i2", "Freelance Project", "workspace", BigDecimal("1250000.0"), "Income")
    )
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    // Nantinya inject Use Cases dari :core:domain di sini:
    // private val getHomeSummaryUseCase: GetHomeSummaryUseCase,
    // private val getChallengeMessageUseCase: GetChallengeMessageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        HomeUiState(
            isLoading = true,
            currentMonthAndYear = getCurrentMonthAndYear(),
            dateRangePeriod = getCurrentDateRange()
        )
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Simulasi pengambilan data
            delay(1500) // Hapus ini saat menggunakan data real

            // Di sini Anda akan memanggil Use Cases:
            // val summaryResult = getHomeSummaryUseCase()
            // val challengeResult = getChallengeMessageUseCase()
            // Berdasarkan hasil, update _uiState (termasuk penanganan error)

            // Untuk sekarang, gunakan data dummy:
            val dummyChallenge = if (Math.random() < 0.5) "Your first challenge is complete!" else null

            _uiState.update {
                it.copy(
                    isLoading = false,
                    summary = DUMMY_HOME_SUMMARY_DATA,
                    challengeMessage = dummyChallenge,
                    // Update tanggal berdasarkan logika sebenarnya jika diperlukan
                    currentMonthAndYear = getCurrentMonthAndYear(),
                    dateRangePeriod = getCurrentDateRange()
                )
            }
        }
    }

    fun onDateRangeClicked() {
        // TODO: Tampilkan dialog pemilih tanggal atau navigasi ke layar pilih periode
        // Setelah periode baru dipilih, panggil loadData() lagi
        println("HomeViewModel: Date range selection triggered.")
        // Contoh: _uiState.update { it.copy(currentMonthAndYear = "July 2025", dateRangePeriod = "01 - 31 July 2025") }
        // loadData() // Panggil ulang dengan parameter periode baru jika ada
    }

    fun onNotificationsClicked() {
        // TODO: Navigasi ke layar notifikasi atau tampilkan dialog/bottom sheet
        println("HomeViewModel: Notifications clicked.")
    }

    fun onSummaryActionClicked() {
        // TODO: Navigasi ke layar laporan detail atau ringkasan keuangan
        println("HomeViewModel: Summary action clicked.")
    }

    fun onMoreOptionsClicked() {
        // TODO: Tampilkan menu dropdown dengan opsi lebih lanjut
        println("HomeViewModel: More options clicked.")
    }

    fun onBalanceCardClicked() {

    }
}