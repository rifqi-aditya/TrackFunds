package com.rifqi.trackfunds.core.data.local.dto

import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDto(
    @Embedded
    val transaction: TransactionEntity,

    @Embedded(prefix = "category_")
    val category: CategoryInfo?,

    @Embedded(prefix = "account_")
    val account: AccountInfo,

    @Embedded(prefix = "savings_goal_")
    val savingsGoal: SavingsGoalInfo?
) {
    data class CategoryInfo(
        val name: String?,
        @ColumnInfo(name = "icon_identifier")
        val categoryIconIdentifier: String?
    )

    data class AccountInfo(
        val name: String,
        @ColumnInfo(name = "icon_identifier")
        val accountIconIdentifier: String,
        val balance: BigDecimal? = BigDecimal.ZERO
    )

    data class SavingsGoalInfo(
        val name: String?,
        @ColumnInfo(name = "icon_identifier")
        val iconIdentifier: String?,
        @ColumnInfo(name = "target_date")
        val targetDate: LocalDateTime?,
        @ColumnInfo(name = "target_amount")
        val targetAmount: BigDecimal?,
        @ColumnInfo(name = "current_amount")
        val currentAmount: BigDecimal?,
        @ColumnInfo(name = "is_achieved")
        val isAchieved: Boolean?
    )
}