package com.rifqi.trackfunds.core.domain.savings.model

import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import java.math.BigDecimal

data class GoalDetails(
    val goal: SavingsGoal,
    val transactions: List<Transaction>,
    val averageMonthlySaving: BigDecimal,
    val estimatedCompletion: String
)