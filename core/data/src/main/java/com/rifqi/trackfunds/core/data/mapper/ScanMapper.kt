package com.rifqi.trackfunds.core.data.mapper

import com.rifqi.trackfunds.core.data.remote.dto.ScanResponseDto
import com.rifqi.trackfunds.core.domain.model.ScanResult
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Mapper dari ScanResponseDto (hasil JSON dari backend) ke ScanResult (domain model).
 */
fun ScanResponseDto.toDomain(): ScanResult {
    // Logika untuk menggabungkan tanggal dan waktu
    val transactionDateTime: LocalDateTime? =
        if (this.transactionDate != null && this.transactionTime != null) {
            try {
                val parsedDate = LocalDate.parse(this.transactionDate)
                val parsedTime = LocalTime.parse(this.transactionTime)
                LocalDateTime.of(parsedDate, parsedTime)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }

    return ScanResult(
        amount = this.totalAmount?.toBigDecimal(),
        date = transactionDateTime,
        note = this.merchantName,
        suggestedCategoryKey = this.category
    )
}