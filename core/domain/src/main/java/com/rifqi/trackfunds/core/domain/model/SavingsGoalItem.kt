package com.rifqi.trackfunds.core.domain.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class SavingsGoalItem(
    val id: String,
    val name: String,
    val targetAmount: BigDecimal,
    val currentAmount: BigDecimal,
    val targetDate: LocalDateTime?,
    val iconIdentifier: String,
    val isAchieved: Boolean
) {
    val progress: Float
        get() = if (targetAmount > BigDecimal.ZERO) {
            (currentAmount / targetAmount).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }
}