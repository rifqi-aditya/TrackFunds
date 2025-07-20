package com.rifqi.trackfunds.core.navigation.api

import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.serialization.Serializable

/**
 * Root interface untuk semua destinasi navigasi di dalam aplikasi.
 * Menggunakan nested sealed interface untuk mengelompokkan rute berdasarkan fitur.
 */
@Serializable
sealed interface AppScreen

// --- Rute Awal & Otentikasi ---

/**
 * Layar awal (splash screen) untuk memeriksa status login dan mengarahkan pengguna.
 */
@Serializable
data object Splash : AppScreen

/**
 * Layar untuk alur otentikasi (Login & Register).
 */
@Serializable
data object Auth : AppScreen


// --- Titik Masuk Grafik Navigasi Utama ---

@Serializable
data object HomeGraph : AppScreen

@Serializable
data object TransactionsGraph : AppScreen

@Serializable
data object BudgetsGraph : AppScreen

@Serializable
data object ReportsGraph : AppScreen

@Serializable
data object ProfileGraph : AppScreen

@Serializable
data object SavingsGraph : AppScreen

@Serializable
data object ScanGraph : AppScreen

@Serializable
data object SharedGraph : AppScreen

@Serializable
data object AccountsGraph : AppScreen


// --- Rute-rute di Dalam Setiap Grafik (Nested Routes) ---

/**
 * Kumpulan rute yang berhubungan dengan fitur Home.
 */
@Serializable
sealed interface HomeRoutes : AppScreen {
    @Serializable
    data object Home : HomeRoutes
}

/**
 * Kumpulan rute yang berhubungan dengan fitur Transaksi.
 */
@Serializable
sealed interface TransactionRoutes : AppScreen {
    @Serializable
    data object Transactions : TransactionRoutes

    @Serializable
    data class TransactionDetail(val transactionId: String) : TransactionRoutes

    @Serializable
    data class AddEditTransaction(val transactionId: String? = null) : TransactionRoutes

    @Serializable
    data object FilterTransactions : TransactionRoutes
}

/**
 * Kumpulan rute yang berhubungan dengan fitur Budget.
 */
@Serializable
sealed interface BudgetRoutes : AppScreen {
    @Serializable
    data object Budgets : BudgetRoutes

    @Serializable
    data class AddEditBudget(val period: String? = null, val budgetId: String? = null) :
        BudgetRoutes
}

/**
 * Kumpulan rute yang berhubungan dengan fitur Akun (Wallet).
 */
@Serializable
sealed interface AccountRoutes : AppScreen {
    @Serializable
    data object Accounts : AccountRoutes

    @Serializable
    data class AddEditAccount(val accountId: String? = null) : AccountRoutes
}


/**
 * Kumpulan rute yang berhubungan dengan fitur Profil & Pengaturan.
 */
@Serializable
sealed interface ProfileRoutes : AppScreen {
    @Serializable
    data object Profile : ProfileRoutes

    @Serializable
    data object Settings : ProfileRoutes

    @Serializable
    data object Notifications : ProfileRoutes
}

/**
 * Kumpulan rute yang berhubungan dengan fitur Laporan (Reports).
 */
@Serializable
sealed interface ReportRoutes : AppScreen {
    @Serializable
    data object Report : ReportRoutes
}

/**
 * Kumpulan rute yang berhubungan dengan fitur Tabungan (Savings).
 */
@Serializable
sealed interface SavingsRoutes : AppScreen {
    @Serializable
    data object Savings : SavingsRoutes

    @Serializable
    data class SavingsDetail(val goalId: String) : SavingsRoutes

    @Serializable
    data class AddEditSavingsGoal(val goalId: String? = null) : SavingsRoutes
}

/**
 * Kumpulan rute yang berhubungan dengan fitur Pindai (Scan).
 */
@Serializable
sealed interface ScanRoutes : AppScreen {
    @Serializable
    data object ScanOption : ScanRoutes

    @Serializable
    data object CameraScan : ScanRoutes

    @Serializable
    data class ReceiptPreview(val imageUri: String) : ScanRoutes
}


/**
 * Kumpulan rute yang dapat digunakan bersama (Shared) oleh beberapa fitur.
 */
@Serializable
sealed interface SharedRoutes : AppScreen {
    @Serializable
    data object Categories : SharedRoutes

    @Serializable
    data class AddEditCategory(
        val categoryId: String? = null,
        val type: TransactionType? = null
    ) : SharedRoutes

    @Serializable
    data object SelectAccount : SharedRoutes

    @Serializable
    data class SelectCategory(val transactionType: String) : SharedRoutes

    @Serializable
    data object Transfer : SharedRoutes
}
