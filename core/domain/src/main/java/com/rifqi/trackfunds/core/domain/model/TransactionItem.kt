package com.rifqi.trackfunds.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Parcelize
data class TransactionItem(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val amount: BigDecimal,
    val type: TransactionType, // Menggunakan enum dari domain
    val date: LocalDateTime,
    val categoryId: String, // ID kategori terkait
    val categoryName: String, // Nama kategori untuk ditampilkan
    val iconIdentifier: String?, // Identifier ikon kategori
    val accountId: String // ID akun terkait
) : Parcelable