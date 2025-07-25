package com.rifqi.trackfunds.core.data.local.converter

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Type Converters for Room to handle custom data types.
 */
class Converters {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    // --- Converters for LocalDateTime ---

    @TypeConverter
    fun fromTimestampToLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC) }
    }

    @TypeConverter
    fun localDateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    }

    // --- Converters for LocalDate ---

    @TypeConverter
    fun fromTimestampToLocalDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun localDateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}