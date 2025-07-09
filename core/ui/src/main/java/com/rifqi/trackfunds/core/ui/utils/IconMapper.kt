package com.rifqi.trackfunds.core.ui.utils


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.rifqi.trackfunds.core.ui.R

/**
 * Memetakan String identifier (standardKey) ke @DrawableRes Int.
 * Identifier HARUS cocok dengan nama file drawable Anda.
 */
@DrawableRes
fun mapIconIdentifierToDrawableRes(identifier: String?): Int {
    // Menggunakan R.drawable.ic_placeholder sebagai fallback default
    val fallbackIcon = R.drawable.placeholder

    return when (identifier?.trim()?.lowercase()) {
        // --- KATEGORI PENGELUARAN (EXPENSE) ---
        "food_and_drink" -> R.drawable.food_and_drink
        "groceries" -> R.drawable.groceries
        "transportation" -> R.drawable.transportation
        "fuel" -> R.drawable.fuel
        "shopping" -> R.drawable.shopping
        "utilities" -> R.drawable.utilities
        "housing" -> R.drawable.housing
        "health" -> R.drawable.health
        "insurance" -> R.drawable.insurance
        "personal_care" -> R.drawable.personal_care
        "entertainment" -> R.drawable.entertainment
        "education" -> R.drawable.education
        "travel" -> R.drawable.travel
        "sports_fitness" -> R.drawable.sports_fitness
        "pets" -> R.drawable.pets
        "gifts_donations" -> R.drawable.gifts_donations
        "miscellaneous" -> R.drawable.miscellaneous

        // --- KATEGORI PEMASUKAN (INCOME) ---
        "salary" -> R.drawable.salary
        "bonus" -> R.drawable.bonus
        "investment_income" -> R.drawable.investment
        "gifts_received" -> R.drawable.gifts_received
        "freelance" -> R.drawable.freelance
        "other_income" -> R.drawable.other_income

        // --- IKON UNTUK AKUN & UI LAINNYA ---
        "wallet_account" -> R.drawable.wallet_account
        "bank_account" -> R.drawable.bank_account
        "transfer" -> R.drawable.transfer
        "calendar" -> R.drawable.calendar
        "notifications" -> R.drawable.notification
        "settings" -> R.drawable.settings
        "search" -> R.drawable.search
        "delete" -> R.drawable.delete
        "edit" -> R.drawable.edit
        "check" -> R.drawable.check
        "close" -> R.drawable.close
        "scan_receipt" -> R.drawable.scan_receipt
        "income" -> R.drawable.income
        "expense" -> R.drawable.expense
        "budgets" -> R.drawable.budgets
        "savings" -> R.drawable.money_bag
        "money" -> R.drawable.money

        // --- IKON GENERIK ---
        "home" -> R.drawable.bottom_nav_home
//        "profile" -> R.drawable.profile
//        "report" -> R.drawable.report

        "arrow_back" -> R.drawable.arrow_back
        "arrow_forward" -> R.drawable.arrow_forward
        "arrow_down" -> R.drawable.arrow_down
        "plus" -> R.drawable.plus


        // Fallback jika tidak ada ikon yang cocok
        else -> fallbackIcon
    }
}

/**
 * Composable helper untuk menampilkan ikon dari drawable resource.
 *
 * @param identifier String identifier untuk drawable resource.
 * @param contentDescription Deskripsi konten untuk aksesibilitas.
 * @param modifier Modifier untuk kustomisasi.
 * @param tint Warna opsional untuk ikon. Defaultnya tidak mengubah warna asli.
 */
@Composable
fun DisplayIconFromResource(
    identifier: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified // Parameter opsional untuk tint
) {
    // Anda perlu mengganti ini dengan implementasi pemetaan Anda yang sebenarnya
    val resourceId = mapIconIdentifierToDrawableRes(identifier)

    Image(
        painter = painterResource(id = resourceId),
        contentDescription = contentDescription,
        modifier = modifier,
        // Terapkan tint hanya jika warna ditentukan
        colorFilter = if (tint != Color.Unspecified) ColorFilter.tint(tint) else null
    )
}