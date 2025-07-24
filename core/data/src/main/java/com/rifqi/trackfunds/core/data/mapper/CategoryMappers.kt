package com.rifqi.trackfunds.core.data.mapper

/**
 * This file contains mapper functions for converting between [CategoryEntity] and [CategoryItem] objects.
 */
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.model.CategoryItem

fun CategoryEntity.toDomain(): CategoryItem {
    return CategoryItem(
        id = this.id,
        userUid = this.userUid,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        type = this.type,
        standardKey = this.standardKey
    )
}

/**
 * Converts a [CategoryItem] to a [CategoryEntity].
 *
 * @param userUid The UID of the user who owns this category.
 * @return The corresponding [CategoryEntity].
 */
fun CategoryItem.toEntity(userUid: String): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        userUid = userUid,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        type = this.type,
        standardKey = this.standardKey
    )
}