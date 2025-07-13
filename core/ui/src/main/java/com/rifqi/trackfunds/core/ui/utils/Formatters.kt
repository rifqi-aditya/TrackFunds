package com.rifqi.trackfunds.core.ui.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
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

fun getCurrentMonthAndYear(locale: Locale = Locale("in", "ID")): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", locale)
    return YearMonth.now().format(formatter)
}

fun getCurrentDateRangePair(): Pair<LocalDate, LocalDate> {
    val today = LocalDate.now()
    val startDate = today.with(TemporalAdjusters.firstDayOfMonth())
    val endDate = today.with(TemporalAdjusters.lastDayOfMonth())
    return Pair(startDate, endDate)
}

fun formatDateRangeToString(
    dateRange: Pair<LocalDate, LocalDate>,
    locale: Locale = Locale("in", "ID")
): String {
    val firstDay = dateRange.first
    val lastDay = dateRange.second

    val dayFormatter = DateTimeFormatter.ofPattern("dd", locale)
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMM yy", locale)

    // Jika bulan dan tahunnya sama, formatnya bisa lebih singkat
    if (firstDay.year == lastDay.year && firstDay.month == lastDay.month) {
        return "${firstDay.format(dayFormatter)} - ${lastDay.format(dayFormatter)} ${
            lastDay.format(
                monthYearFormatter
            )
        }"
    }
    // Jika berbeda (jarang terjadi untuk rentang bulanan, tapi bagus untuk jaga-jaga)
    return "${firstDay.format(dayFormatter)} ${firstDay.format(monthYearFormatter)} - ${
        lastDay.format(
            dayFormatter
        )
    } ${lastDay.format(monthYearFormatter)}"
}