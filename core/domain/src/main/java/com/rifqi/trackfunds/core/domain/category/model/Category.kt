package com.rifqi.trackfunds.core.domain.category.model

import android.content.Context
import java.util.UUID

enum class TransactionType {
    EXPENSE, INCOME, SAVINGS
}

data class Category(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val iconIdentifier: String,
    val type: TransactionType,
    val standardKey: String? = null,
    val userUid: String? = null
) {
    companion object {
        fun uncategorized(): Category {
            return Category(
                name = "Uncategorized",
                iconIdentifier = "ic_category_uncategorized",
                type = TransactionType.EXPENSE,
                standardKey = "uncategorized"
            )
        }
    }
}

fun Category.getDisplayName(context: Context): String {
    // Gunakan scope function 'let' untuk mengeksekusi blok kode hanya jika standardKey tidak null.
    return this.standardKey?.let { key ->
        val resourceId = context.resources.getIdentifier(
            key,
            "string",
            context.packageName
        )

        // Jika resource ditemukan (ID bukan 0), gunakan. Jika tidak, gunakan key sebagai fallback.
        if (resourceId != 0) {
            context.getString(resourceId)
        } else {
            key // Fallback jika string resource untuk kunci ini tidak ada
        }
    } ?: this.name // Jika standardKey null, gunakan 'name' dari kategori.
}