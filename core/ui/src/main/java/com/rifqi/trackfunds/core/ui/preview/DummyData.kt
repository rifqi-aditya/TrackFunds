package com.rifqi.trackfunds.core.ui.preview

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.budget.model.Budget
import com.rifqi.trackfunds.core.domain.category.model.Category
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * Object terpusat yang berisi semua data sampel untuk digunakan di seluruh aplikasi.
 * Menjadi satu-satunya sumber kebenaran (Single Source of Truth) untuk data preview.
 */
object DummyData {
    val dummyAccount1 =
        Account("acc1", "Dompet Digital", "wallet_account", BigDecimal("1500000"))
    val dummyAccount2 =
        Account("acc2", "Rekening Utama", "bank_account", BigDecimal("10000000"))
    val dummyAccounts = listOf(dummyAccount1, dummyAccount2)

    val dummyCategory1 =
        Category("cat1", "Makan & Minum", "restaurant", TransactionType.EXPENSE)
    val dummyCategory2 = Category("cat2", "Gaji", "salary", TransactionType.INCOME)

    val dummyTransactions = listOf(
        Transaction(
            "1",
            BigDecimal("50000"),
            TransactionType.EXPENSE,
            LocalDateTime.now(),
            "Kopi Pagi",
            dummyCategory1,
            dummyAccount1
        ),
        Transaction(
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
        Budget(
            "b1",
            "Makan & Minum",
            dummyCategory1.name,
            dummyCategory1.iconIdentifier,
            BigDecimal("2000000"),
            BigDecimal("1250000"),
            YearMonth.now()
        ),
        Budget(
            "b2",
            "Transportasi",
            dummyCategory2.name,
            dummyCategory2.iconIdentifier,
            BigDecimal("2000000"),
            BigDecimal("1250000"),
            YearMonth.now()
        )
    )
}