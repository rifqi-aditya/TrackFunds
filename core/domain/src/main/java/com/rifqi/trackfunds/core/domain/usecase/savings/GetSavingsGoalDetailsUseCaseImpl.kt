package com.rifqi.trackfunds.core.domain.usecase.savings

import com.rifqi.trackfunds.core.domain.model.GoalDetails
import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import javax.inject.Inject
import kotlin.math.ceil

class GetSavingsGoalDetailsUseCaseImpl @Inject constructor(
    private val savingsGoalRepository: SavingsGoalRepository,
    private val transactionRepository: TransactionRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : GetSavingsGoalDetailsUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override operator fun invoke(goalId: String): Flow<GoalDetails?> {
        return userPreferencesRepository.userUid.flatMapLatest { userUid ->
            if (userUid == null) {
                return@flatMapLatest flowOf(null)
            }

            // 2. Ambil Flow untuk goal dan Flow untuk transaksi
            val goalFlow = savingsGoalRepository.getGoalById(userUid, goalId)
            val transactionsFlow = transactionRepository.getTransactionsForGoal(userUid, goalId)

            // 3. Gabungkan kedua Flow tersebut
            combine(goalFlow, transactionsFlow) { goal, transactions ->
                if (goal == null) {
                    return@combine null // Jika goal tidak ditemukan, kirim null
                }

                // 4. Lakukan semua kalkulasi bisnis di sini
                val average =
                    calculateAverageMonthlySavings(transactions, goal.targetDate ?: LocalDate.now())
                val remaining = goal.targetAmount - goal.savedAmount
                val estimation = calculateEstimatedCompletion(remaining, average)


                GoalDetails(
                    goal = goal,
                    transactions = transactions,
                    averageMonthlySaving = average,
                    estimatedCompletion = estimation
                )
            }
        }
    }

    // --- Logika Kalkulasi (Private di dalam Use Case) ---

    private fun calculateAverageMonthlySavings(
        transactions: List<Transaction>,
        goalStartDate: LocalDate
    ): BigDecimal {
        val totalContribution = transactions
            .filter { it.amount > BigDecimal.ZERO }
            .sumOf { it.amount }

        if (totalContribution <= BigDecimal.ZERO) return BigDecimal.ZERO

        val monthsElapsed = ChronoUnit.MONTHS.between(goalStartDate, LocalDate.now())
            .let { if (it == 0L) 1L else it }

        return totalContribution.divide(BigDecimal.valueOf(monthsElapsed), 2, RoundingMode.HALF_UP)
    }

    private fun calculateEstimatedCompletion(
        remainingAmount: BigDecimal,
        averageMonthlySaving: BigDecimal
    ): String {
        if (averageMonthlySaving <= BigDecimal.ZERO) return "N/A"
        if (remainingAmount <= BigDecimal.ZERO) return "Completed!"

        val monthsNeededDecimal =
            remainingAmount.divide(averageMonthlySaving, 8, RoundingMode.HALF_UP)
        val monthsNeeded = ceil(monthsNeededDecimal.toDouble()).toLong()
        val completionDate = LocalDate.now().plusMonths(monthsNeeded)

        val formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH)
        return "approx. ${completionDate.format(formatter)}"
    }
}