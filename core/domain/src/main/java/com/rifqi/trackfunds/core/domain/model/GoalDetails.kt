package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal

data class GoalDetails(
    val goal: SavingsGoal,
    val transactions: List<Transaction>,
    val averageMonthlySaving: BigDecimal,
    val estimatedCompletion: String
)