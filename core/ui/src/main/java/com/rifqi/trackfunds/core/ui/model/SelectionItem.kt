package com.rifqi.trackfunds.core.ui.model

data class SelectionItem(
    val id: String,
    val name: String,
    val iconIdentifier: String?,
    val description: String? = null,
    val selectedItem: String? = null,
)