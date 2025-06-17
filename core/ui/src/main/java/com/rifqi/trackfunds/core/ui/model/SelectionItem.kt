package com.rifqi.trackfunds.core.ui.model

// Ini Item yang digunakan untuk menampilkan pilihan di layar reusable selection
data class SelectionItem(
    val id: String,
    val name: String,
    val iconIdentifier: String?,
    val description: String? = null,
)