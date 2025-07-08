package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(name = "target_amount")
    val targetAmount: BigDecimal,
    @ColumnInfo(name = "current_amount")
    val currentAmount: BigDecimal = BigDecimal.ZERO,
    @ColumnInfo(name = "target_date")
    val targetDate: LocalDateTime?,
    @ColumnInfo(name = "icon_identifier")
    val iconIdentifier: String,
    @ColumnInfo(name = "is_achieved")
    val isAchieved: Boolean = false
)