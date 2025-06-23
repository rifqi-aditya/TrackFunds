package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.BudgetWithDetailsDto
import com.rifqi.trackfunds.core.data.local.entity.BudgetEntity
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import java.math.BigDecimal

fun BudgetWithDetailsDto.toDomain(): BudgetItem {
    return BudgetItem(
        budgetId = this.budgetId,
        categoryId = this.categoryId,
        categoryName = this.categoryName,
        categoryIconIdentifier = this.categoryIconIdentifier,
        budgetAmount = this.budgetAmount,
        spentAmount = this.spentAmount,
        period = this.period
    )
}

fun BudgetItem.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = this.budgetId,
        categoryId = this.categoryId,
        amount = this.budgetAmount ?: BigDecimal.ZERO,
        period = this.period
    )
}