package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class SavingsGoal(
    val id: String,
    val name: String,
    val targetAmount: BigDecimal,
    val savedAmount: BigDecimal,
    val targetDate: LocalDate?,
    val iconIdentifier: String,
    val isAchieved: Boolean
) {
    val progress: Float
        get() = if (targetAmount > BigDecimal.ZERO) {
            (savedAmount / targetAmount).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }

    val remainingAmount: BigDecimal
        get() = targetAmount - savedAmount
}