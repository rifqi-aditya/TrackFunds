package com.rifqi.trackfunds.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(name = "icon_identifier")
    val iconIdentifier: String,
    val type: String
)