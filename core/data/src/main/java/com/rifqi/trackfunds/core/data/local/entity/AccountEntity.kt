package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = "accounts",
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
data class AccountEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_uid") val userUid: String,
    val name: String,
    @ColumnInfo(name = "icon_identifier") val iconIdentifier: String?,
    val balance: BigDecimal = BigDecimal.ZERO,
)