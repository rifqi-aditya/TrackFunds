package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.dto.CashFlowDto
import com.rifqi.trackfunds.core.data.local.dto.CategorySpendingDto
import com.rifqi.trackfunds.core.domain.model.CashFlowSummary
import com.rifqi.trackfunds.core.domain.model.CategorySpending
import java.math.BigDecimal

fun CategorySpendingDto.toDomain(): CategorySpending {
    return CategorySpending(
        categoryName = this.categoryName,
        totalAmount = this.totalAmount
    )
}

fun CashFlowDto.toDomain(): CashFlowSummary {
    val income = this.totalIncome ?: BigDecimal.ZERO
    val expense = this.totalExpense ?: BigDecimal.ZERO
    return CashFlowSummary(
        totalIncome = income,
        totalExpense = expense,
        netCashFlow = income - expense
    )
}