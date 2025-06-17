package com.rifqi.trackfunds.core.data.local.converter

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Type Converters untuk memberitahu Room cara menangani tipe data kustom.
 */
class Converters {

    /**
     * Mengubah BigDecimal menjadi String untuk disimpan di database.
     * Menggunakan String menjaga presisi angka.
     * @return String representasi dari BigDecimal, atau null jika input null.
     */
    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? {
        return value?.toPlainString()
    }

    /**
     * Mengubah String dari database kembali menjadi BigDecimal.
     * @return BigDecimal dari String, atau null jika input null atau kosong.
     */
    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(it) }
    }

    // --- Converter BARU untuk LocalDateTime ---
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        // Mengubah Long (timestamp) dari database menjadi LocalDateTime
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        // Mengubah LocalDateTime menjadi Long (timestamp) untuk disimpan di database
        return date?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    }
}