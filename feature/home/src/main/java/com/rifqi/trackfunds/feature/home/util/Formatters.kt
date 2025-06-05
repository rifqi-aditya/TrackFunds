package com.rifqi.trackfunds.feature.home.util

import java.text.NumberFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

fun formatCurrency(amount: Double, locale: Locale = Locale("in", "ID")): String {
    val currencyFormat = NumberFormat.getCurrencyInstance(locale)
    currencyFormat.maximumFractionDigits = 0
    currencyFormat.minimumFractionDigits = 0
    return currencyFormat.format(amount)
}

fun getCurrentMonthAndYear(locale: Locale = Locale("in", "ID")): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", locale)
    return YearMonth.now().format(formatter)
}

fun getCurrentDateRange(locale: Locale = Locale("in", "ID")): String {
    val now = YearMonth.now()
    val firstDay = now.atDay(1)
    val lastDay = now.atEndOfMonth()
    val dayFormatter = DateTimeFormatter.ofPattern("dd", locale)
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMM yy", locale) // Contoh: Jun 25

    return "${firstDay.format(dayFormatter)} ${firstDay.format(monthYearFormatter)} - ${
        lastDay.format(
            dayFormatter
        )
    } ${lastDay.format(monthYearFormatter)}"
}