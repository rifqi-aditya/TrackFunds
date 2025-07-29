package com.rifqi.trackfunds.core.ui.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatCurrency(amount: BigDecimal, locale: Locale = Locale("in", "ID")): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    // Ubah simbol "Rp" menjadi "Rp "
    (currencyFormat as DecimalFormat).let {
        val symbols = it.decimalFormatSymbols
        symbols.currencySymbol = "Rp "
        it.decimalFormatSymbols = symbols
    }
    currencyFormat.maximumFractionDigits = 0
    return currencyFormat.format(amount)
}


/**
 * Mengubah objek LocalDate menjadi String dengan format "29 Jul 2025".
 * @param date Objek LocalDate yang akan diformat.
 * @return String tanggal yang sudah diformat.
 */
fun formatLocalDate(date: LocalDate): String {
    // Tentukan pola format yang diinginkan
    // dd = hari (01-31), MMM = nama bulan singkat (Jan-Dec), yyyy = tahun
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
    return date.format(formatter)
}

/**
 * Mengubah objek LocalDateTime menjadi String dengan format "29 Jul 2025, 21:33".
 * @param dateTime Objek LocalDateTime yang akan diformat.
 * @return String tanggal dan waktu yang sudah diformat.
 */
fun formatLocalDateTime(dateTime: LocalDateTime): String {
    // Tentukan pola format yang diinginkan
    // HH = jam (00-23), mm = menit (00-59)
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.ENGLISH)
    return dateTime.format(formatter)
}