package com.rifqi.trackfunds.core.data.local.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

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

    // Anda bisa menambahkan converter lain di sini jika perlu,
    // misalnya untuk LocalDate, dll.
}