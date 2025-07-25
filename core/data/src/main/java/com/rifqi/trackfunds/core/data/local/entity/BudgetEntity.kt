package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(
    tableName = "budgets",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["uid"],
            childColumns = ["user_uid"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_uid"]),
        Index(value = ["category_id"])
    ]
)
data class BudgetEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "user_uid")
    val userUid: String,

    @ColumnInfo(name = "category_id")
    val categoryId: String,

    val amount: BigDecimal,
    val period: LocalDate
)