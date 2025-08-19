package com.rifqi.trackfunds.feature.transaction.ui.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rifqi.trackfunds.core.common.NavigationResultManager
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.category.model.CategoryFilter
import com.rifqi.trackfunds.core.domain.category.usecase.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.common.model.DateRangeOption
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject


// ===== UI models (tetap sederhana) =====
data class CategoryData(val id: String, val name: String, val iconIdentifier: String)
data class AccountData(val id: String, val name: String, val iconIdentifier: String)

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getFilteredCategoriesUseCase: GetFilteredCategoriesUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val resultManager: NavigationResultManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FilterUiState())
    val uiState: StateFlow<FilterUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<FilterEffect>()
    val effect: SharedFlow<FilterEffect> = _effect.asSharedFlow()

    init {
        // Muat pilihan (kategori, akun)
        loadChoiceData()

        // Ambil filter awal dari navigation argument (jika ada)
        val initial = resultManager.argument.value as? TransactionFilter
        if (initial != null) {
            applyInitialFilter(initial)
            resultManager.setArgument(null)
        }
    }

    private fun loadChoiceData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            combine(
                getFilteredCategoriesUseCase(CategoryFilter()),
                getAccountsUseCase()
            ) { cats, accs ->
                // Map domain → UI (sesuaikan field emoji/iconIdentifier kalau ada)
                val uiCats = cats.map {
                    CategoryData(
                        id = it.id,
                        name = it.name,
                        iconIdentifier = it.iconIdentifier
                    )
                }
                val uiAccs = accs.map {
                    AccountData(
                        id = it.id,
                        name = it.name,
                        iconIdentifier = it.iconIdentifier
                    )
                }
                _uiState.update { s ->
                    s.copy(isLoading = false, allCategories = uiCats, allAccounts = uiAccs)
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }.collect()
        }
    }

    fun onIntent(intent: FilterIntent) {
        when (intent) {
            FilterIntent.ResetClicked -> {
                _uiState.update {
                    it.copy(
                        selectedCategoryIds = emptySet(),
                        selectedAccountIds = emptySet(),
                        selectedTypes = emptySet(),
                        dateOption = DateRangeOption.THIS_MONTH,
                        customStart = null,
                        customEnd = null,
                        showAllCategories = false
                    )
                }
            }

            FilterIntent.CancelClicked -> emitEffect(FilterEffect.NavigateBack)

            FilterIntent.ApplyClicked -> applyAndReturn()

            is FilterIntent.CategoryToggled -> {
                toggleInSet(_uiState.value.selectedCategoryIds, intent.id) { newSet ->
                    _uiState.update { it.copy(selectedCategoryIds = newSet) }
                }
            }

            is FilterIntent.AccountToggled -> {
                toggleInSet(_uiState.value.selectedAccountIds, intent.id) { newSet ->
                    _uiState.update { it.copy(selectedAccountIds = newSet) }
                }
            }

            is FilterIntent.TypeToggled -> {
                val cur = _uiState.value.selectedTypes.toMutableSet()
                if (!cur.add(intent.type)) cur.remove(intent.type)
                _uiState.update { it.copy(selectedTypes = cur) }
            }

            is FilterIntent.DateOptionSelected -> {
                _uiState.update { it.copy(dateOption = intent.option) }
                if (intent.option == DateRangeOption.CUSTOM) {
                    emitEffect(FilterEffect.OpenDateRangePicker)
                } else {
                    // kosongkan custom range agar tidak ambigu
                    _uiState.update { it.copy(customStart = null, customEnd = null) }
                }
            }

            is FilterIntent.CustomDateChanged -> {
                _uiState.update {
                    it.copy(
                        dateOption = DateRangeOption.CUSTOM,
                        customStart = intent.start,
                        customEnd = intent.end
                    )
                }
            }

            FilterIntent.ToggleShowAllCategories -> {
                _uiState.update { it.copy(showAllCategories = !it.showAllCategories) }
            }
        }
    }

    private fun applyAndReturn() {
        val s = _uiState.value
        val (startDate, endDate) = calculateDateRange(
            option = s.dateOption,
            customStart = s.customStart,
            customEnd = s.customEnd
        )

        val finalFilter = TransactionFilter(
            categoryIds = s.selectedCategoryIds.toList().ifEmpty { null },
            accountIds = s.selectedAccountIds.toList().ifEmpty { null },
            startDate = startDate,
            endDate = endDate
            // jika domain-mu perlu tipe transaksi, tambahkan properti di TransactionFilter domain
        )

        resultManager.setResult(finalFilter)
        emitEffect(FilterEffect.NavigateBack)
    }

    private fun applyInitialFilter(initial: TransactionFilter) {
        // Set category/account yang sudah ada
        _uiState.update {
            it.copy(
                selectedCategoryIds = initial.categoryIds?.toSet() ?: emptySet(),
                selectedAccountIds = initial.accountIds?.toSet() ?: emptySet()
            )
        }

        // Derive date option dari start/end (jika tersedia), fallback ALL_TIME
        val now = LocalDate.now()
        val start = initial.startDate
        val end = initial.endDate

        if (start == null && end == null) {
            _uiState.update {
                it.copy(
                    dateOption = DateRangeOption.ALL_TIME,
                    customStart = null,
                    customEnd = null
                )
            }
            return
        }
        if (start != null && end != null) {
            val guessed = guessDateOptionFromRange(now, start, end)
            if (guessed != null && guessed != DateRangeOption.CUSTOM) {
                _uiState.update {
                    it.copy(
                        dateOption = guessed,
                        customStart = null,
                        customEnd = null
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        dateOption = DateRangeOption.CUSTOM,
                        customStart = start,
                        customEnd = end
                    )
                }
            }
        } else {
            // salah satu null → anggap custom
            _uiState.update {
                it.copy(
                    dateOption = DateRangeOption.CUSTOM,
                    customStart = start,
                    customEnd = end
                )
            }
        }
    }

    private fun guessDateOptionFromRange(
        now: LocalDate,
        start: LocalDate,
        end: LocalDate
    ): DateRangeOption? {
        // THIS_MONTH
        val mStart = now.withDayOfMonth(1)
        val mEnd = now.with(TemporalAdjusters.lastDayOfMonth())
        if (start == mStart && end == mEnd) return DateRangeOption.THIS_MONTH

        // LAST_MONTH
        val lm = now.minusMonths(1)
        val lmStart = lm.withDayOfMonth(1)
        val lmEnd = lm.with(TemporalAdjusters.lastDayOfMonth())
        if (start == lmStart && end == lmEnd) return DateRangeOption.LAST_MONTH

        // THIS_YEAR
        val yStart = now.withDayOfYear(1)
        val yEnd = now.with(TemporalAdjusters.lastDayOfYear())
        if (start == yStart && end == yEnd) return DateRangeOption.THIS_YEAR

        // THIS_WEEK (Senin-Minggu)
        val wStart = now.with(DayOfWeek.MONDAY)
        val wEnd = wStart.plusDays(6)
        if (start == wStart && end == wEnd) return DateRangeOption.THIS_WEEK

        // ALL_TIME: tidak cocok karena dua tanggal terisi → bukan all time
        // CUSTOM jika tak ada yang cocok
        return DateRangeOption.CUSTOM
    }

    private fun calculateDateRange(
        option: DateRangeOption,
        customStart: LocalDate?,
        customEnd: LocalDate?
    ): Pair<LocalDate?, LocalDate?> {
        val today = LocalDate.now()
        return when (option) {
            DateRangeOption.THIS_WEEK -> {
                val start = today.with(DayOfWeek.MONDAY)
                start to start.plusDays(6)
            }

            DateRangeOption.THIS_MONTH -> {
                val start = today.withDayOfMonth(1)
                start to today.with(TemporalAdjusters.lastDayOfMonth())
            }

            DateRangeOption.LAST_MONTH -> {
                val last = today.minusMonths(1)
                val start = last.withDayOfMonth(1)
                start to last.with(TemporalAdjusters.lastDayOfMonth())
            }

            DateRangeOption.THIS_YEAR -> {
                val start = today.withDayOfYear(1)
                start to today.with(TemporalAdjusters.lastDayOfYear())
            }

            DateRangeOption.ALL_TIME -> null to null
            DateRangeOption.CUSTOM -> customStart to customEnd
        }
    }

    private fun emitEffect(e: FilterEffect) {
        viewModelScope.launch { _effect.emit(e) }
    }

    private inline fun <T> toggleInSet(
        current: Set<T>,
        item: T,
        crossinline onUpdate: (Set<T>) -> Unit
    ) {
        val m = current.toMutableSet()
        if (!m.add(item)) m.remove(item)
        onUpdate(m)
    }
}