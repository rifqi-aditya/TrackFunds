package com.rifqi.trackfunds.feature.transaction.ui.model

import java.util.UUID

// Model UI untuk satu baris item
data class LineItemUiModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val quantity: String = "1",
    val price: String = ""
)