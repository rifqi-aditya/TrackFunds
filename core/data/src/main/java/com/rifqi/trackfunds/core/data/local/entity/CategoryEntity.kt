package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rifqi.trackfunds.core.domain.category.model.TransactionType


@Entity(
    tableName = "categories",
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
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_uid")
    val userUid: String? = null,
    val name: String,
    @ColumnInfo(name = "icon_identifier")
    val iconIdentifier: String,
    val type: TransactionType,
    @ColumnInfo(name = "standard_key", index = true)
    val standardKey: String? = null
)