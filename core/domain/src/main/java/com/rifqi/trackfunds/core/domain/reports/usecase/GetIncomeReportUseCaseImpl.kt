package com.rifqi.trackfunds.core.domain.reports.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.reports.model.CategorySummary
import com.rifqi.trackfunds.core.domain.reports.model.IncomeReport
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetIncomeReportUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetIncomeReportUseCase {

    override operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<IncomeReport> {
        val filter = TransactionFilter(
            startDate = startDate,
            endDate = endDate,
            type = TransactionType.INCOME
        )

        return transactionRepository.getFilteredTransactions(filter)
            .map { incomeTransactions ->
                if (incomeTransactions.isEmpty()) {
                    return@map IncomeReport(emptyList())
                }

                val totalIncome = incomeTransactions.sumOf { it.amount }

                val summaries = incomeTransactions
                    .groupBy { it.category }
                    .map { (category, transactions) ->
                        val categoryTotal = transactions.sumOf { it.amount }
                        CategorySummary(
                            category = category ?: Category.uncategorized(),
                            totalAmount = categoryTotal,
                            percentage = (categoryTotal.toDouble() / totalIncome.toDouble()).toFloat()
                        )
                    }
                    .sortedByDescending { it.totalAmount }

                IncomeReport(categorySummaries = summaries)
            }
    }
}