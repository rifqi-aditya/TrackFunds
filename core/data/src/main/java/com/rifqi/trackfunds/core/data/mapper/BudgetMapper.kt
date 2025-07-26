package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.BudgetWithDetailsDto
import com.rifqi.trackfunds.core.data.local.entity.BudgetEntity
import com.rifqi.trackfunds.core.domain.model.BudgetModel
import java.math.BigDecimal
import java.time.YearMonth

/**
 * Converts a [BudgetWithDetailsDto] to a [BudgetModel] domain model.
 * @return The [BudgetModel] representation of the DTO.
 */
fun BudgetWithDetailsDto.toDomain(): BudgetModel {
    return BudgetModel(
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
 * Converts a [BudgetModel] domain model to a [BudgetEntity].
 * @param userUid The UID of the user associated with this budget.
 * @return The [BudgetEntity] representation of the domain model.
 */
fun BudgetModel.toEntity(userUid: String): BudgetEntity {
    return BudgetEntity(
        id = this.budgetId,
        userUid = userUid,
        categoryId = this.categoryId,
        amount = this.budgetAmount,
        period = this.period.atDay(1)
    )
}