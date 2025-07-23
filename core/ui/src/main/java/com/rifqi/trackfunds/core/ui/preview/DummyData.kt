package com.rifqi.trackfunds.core.ui.preview

import com.rifqi.trackfunds.core.domain.model.AccountItem
import com.rifqi.trackfunds.core.domain.model.BudgetItem
import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Object terpusat yang berisi semua data sampel untuk digunakan di seluruh aplikasi.
 * Menjadi satu-satunya sumber kebenaran (Single Source of Truth) untuk data preview.
 */
object DummyData {
    val dummyAccount1 =
        AccountItem("acc1", "Dompet Digital", "wallet_account", BigDecimal("1500000"))
    val dummyAccount2 =
        AccountItem("acc2", "Rekening Utama", "bank_account", BigDecimal("10000000"))
    val dummyAccounts = listOf(dummyAccount1, dummyAccount2)

    val dummyCategory1 =
        CategoryItem("cat1", "Makan & Minum", "restaurant", TransactionType.EXPENSE)
    val dummyCategory2 = CategoryItem("cat2", "Gaji", "salary", TransactionType.INCOME)

    val dummyTransactions = listOf(
        TransactionItem(
            "1",
            BigDecimal("50000"),
            TransactionType.EXPENSE,
            LocalDateTime.now(),
            "Kopi Pagi",
            dummyCategory1,
            dummyAccount1
        ),
        TransactionItem(
            "2",
            BigDecimal("7500000"),
            TransactionType.INCOME,
            LocalDateTime.now().minusDays(1),
            "Gaji Bulanan",
            dummyCategory2,
            dummyAccount2
        )
    )

    val dummyBudgets = listOf(
        BudgetItem(
            "b1",
            "Makan & Minum",
            dummyCategory1.name,
            dummyCategory1.iconIdentifier,
            BigDecimal("2000000"),
            BigDecimal("1250000"),
            "07-2025"
        ),
        BudgetItem(
            "b2",
            "Transportasi",
            dummyCategory2.name,
            dummyCategory2.iconIdentifier,
            BigDecimal("2000000"),
            BigDecimal("1250000"),
            "08-2025"
        )
    )
}