package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(
    tableName = "savings_goals",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["user_uid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_uid"])]
)
data class SavingsGoalEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_uid")
    val userUid: String,

    val name: String,

    @ColumnInfo(name = "target_amount")
    val targetAmount: BigDecimal,

    @ColumnInfo(name = "saved_amount")
    val savedAmount: BigDecimal = BigDecimal.ZERO,

    @ColumnInfo(name = "target_date")
    val targetDate: LocalDate?,

    @ColumnInfo(name = "icon_identifier")
    val iconIdentifier: String,

    @ColumnInfo(name = "is_achieved")
    val isAchieved: Boolean = false
)