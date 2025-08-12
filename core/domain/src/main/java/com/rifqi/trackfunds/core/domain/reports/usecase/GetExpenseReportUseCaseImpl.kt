package com.rifqi.trackfunds.core.domain.reports.usecase

import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import com.rifqi.trackfunds.core.domain.reports.model.CategorySummary
import com.rifqi.trackfunds.core.domain.reports.model.ExpenseReport
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetExpenseReportUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : GetExpenseReportUseCase {

    override operator fun invoke(startDate: LocalDate, endDate: LocalDate): Flow<ExpenseReport> {
        val filter = TransactionFilter(
            startDate = startDate,
            endDate = endDate,
            type = TransactionType.EXPENSE
        )

        return transactionRepository.getFilteredTransactions(filter)
            .map { expenseTransactions ->
                if (expenseTransactions.isEmpty()) {
                    return@map ExpenseReport(emptyList())
                }

                val totalExpense = expenseTransactions.sumOf { it.amount }

                val summaries = expenseTransactions
                    .groupBy { it.category }
                    .map { (category, transactions) ->
                        val categoryTotal = transactions.sumOf { it.amount }
                        CategorySummary(
                            category = category ?: Category.uncategorized(),
                            totalAmount = categoryTotal,
                            percentage = (categoryTotal.toDouble() / totalExpense.toDouble()).toFloat()
                        )
                    }
                    .sortedByDescending { it.totalAmount }

                ExpenseReport(categorySummaries = summaries)
            }
    }
}