package com.rifqi.trackfunds.core.domain.reports.usecase

import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowPeriodOption
import com.rifqi.trackfunds.core.domain.reports.model.CashFlowReport
import com.rifqi.trackfunds.core.domain.reports.model.MonthlyFlow
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

class GetCashFlowReportUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetCashFlowReportUseCase {

    override fun invoke(periodOption: CashFlowPeriodOption): Flow<CashFlowReport> {
        val today = LocalDate.now()

        // 1. Tentukan rentang tanggal berdasarkan pilihan enum
        val (startDate, endDate) = when (periodOption) {
            CashFlowPeriodOption.LAST_3_MONTHS -> today.minusMonths(2).withDayOfMonth(1) to today
            CashFlowPeriodOption.LAST_6_MONTHS -> today.minusMonths(5).withDayOfMonth(1) to today
            CashFlowPeriodOption.LAST_12_MONTHS -> today.minusMonths(11).withDayOfMonth(1) to today
            CashFlowPeriodOption.THIS_YEAR -> today.withDayOfYear(1) to today

        }

        val filter = TransactionFilter(startDate = startDate, endDate = endDate)

        // 2. Sisa logika untuk mengambil dan memproses data tetap sama
        return transactionRepository.getFilteredTransactions(filter)
            .map { transactions ->
                // 3. Kelompokkan transaksi berdasarkan Bulan & Tahun
                val monthlyData = transactions.groupBy { YearMonth.from(it.date) }

                // --- PERBAIKAN DI SINI ---
                // 4. Buat daftar semua bulan dalam rentang tanggal yang sebenarnya
                val allMonthsInRange = mutableListOf<YearMonth>()
                var currentMonth = YearMonth.from(startDate)
                val endMonth = YearMonth.from(endDate)

                while (!currentMonth.isAfter(endMonth)) {
                    allMonthsInRange.add(currentMonth)
                    currentMonth = currentMonth.plusMonths(1)
                }

                // 5. Proses setiap bulan untuk menghitung total pemasukan & pengeluaran
                val monthlyFlows = allMonthsInRange.map { month ->
                    val transactionsInMonth = monthlyData[month] ?: emptyList()

                    val income = transactionsInMonth
                        .filter { it.type == TransactionType.INCOME }
                        .sumOf { it.amount }

                    val expense = transactionsInMonth
                        .filter { it.type == TransactionType.EXPENSE }
                        .sumOf { it.amount }

                    MonthlyFlow(
                        period = month,
                        income = income,
                        expense = expense
                    )
                }

                CashFlowReport(monthlyFlows = monthlyFlows)
            }
    }
}
