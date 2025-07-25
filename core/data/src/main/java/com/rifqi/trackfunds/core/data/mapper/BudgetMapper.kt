package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.BudgetWithDetailsDto
import com.rifqi.trackfunds.core.data.local.entity.BudgetEntity
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import java.math.BigDecimal
import java.time.YearMonth

/**
 * Converts a [BudgetWithDetailsDto] to a [BudgetItem] domain model.
 * @return The [BudgetItem] representation of the DTO.
 */
fun BudgetWithDetailsDto.toDomain(): BudgetItem {
    return BudgetItem(
        budgetId = this.budgetId,
        categoryId = this.categoryId,
        categoryName = this.categoryName ?: "",
        categoryIconIdentifier = this.categoryIconIdentifier,
        budgetAmount = this.budgetAmount ?: BigDecimal.ZERO,
        spentAmount = this.spentAmount,
        period = YearMonth.from(this.period)
    )
}

/**
 * Converts a [BudgetItem] domain model to a [BudgetEntity].
 * @param userUid The UID of the user associated with this budget.
 * @return The [BudgetEntity] representation of the domain model.
 */
fun BudgetItem.toEntity(userUid: String): BudgetEntity {
    return BudgetEntity(
        id = this.budgetId,
        userUid = userUid,
        categoryId = this.categoryId,
        amount = this.budgetAmount,
        period = this.period.atDay(1)
    )
}