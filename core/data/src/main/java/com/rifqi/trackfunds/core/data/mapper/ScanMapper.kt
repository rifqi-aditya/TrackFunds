package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.remote.dto.ScanResponseDto
import com.rifqi.trackfunds.core.domain.model.ScanResult
import java.time.LocalDate

/**
 * Mapper dari ScanResponseDto (hasil JSON dari backend) ke ScanResult (domain model).
 */
fun ScanResponseDto.toDomain(): ScanResult {
    return ScanResult(
        // Konversi Double? dari JSON menjadi BigDecimal? yang lebih presisi
        amount = this.totalAmount?.toBigDecimal(),

        // Parsing String "YYYY-MM-DD" menjadi objek LocalDate
        // .let digunakan untuk memastikan parsing hanya terjadi jika string tidak null
        date = this.transactionDate?.let { LocalDate.parse(it) },

        // Mapping langsung karena tipenya sudah cocok
        note = this.detectedMerchant,
        suggestedCategoryKey = this.suggestedCategoryKey
    )
}