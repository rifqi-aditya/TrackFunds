package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.model.CategoryItem

// Mapper dari CategoryEntity (data layer) ke CategoryItem (domain layer)
fun CategoryEntity.toDomain(): CategoryItem {
    return CategoryItem(
        id = this.id,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        type = this.type
    )
}

// Mapper dari CategoryItem (domain) ke CategoryEntity (data)
// Mungkin dibutuhkan jika Anda ingin menyimpan perubahan dari domain ke database
fun CategoryItem.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name,
        iconIdentifier = this.iconIdentifier,
        type = this.type
    )
}