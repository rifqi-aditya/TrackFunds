package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "icon_identifier") val iconIdentifier: String?,
    val balance: BigDecimal
)