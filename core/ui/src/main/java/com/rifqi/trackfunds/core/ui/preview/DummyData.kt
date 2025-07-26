package com.rifqi.trackfunds.core.ui.preview

import com.rifqi.trackfunds.core.domain.model.AccountModel
import com.rifqi.trackfunds.core.domain.model.BudgetModel
import com.rifqi.trackfunds.core.domain.model.CategoryModel
import com.rifqi.trackfunds.core.domain.model.TransactionModel
import com.rifqi.trackfunds.core.domain.model.TransactionType
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.YearMonth

/**
 * Object terpusat yang berisi semua data sampel untuk digunakan di seluruh aplikasi.
 * Menjadi satu-satunya sumber kebenaran (Single Source of Truth) untuk data preview.
 */
object DummyData {
    val dummyAccount1 =
        AccountModel("acc1", "Dompet Digital", "wallet_account", BigDecimal("1500000"))
    val dummyAccount2 =
        AccountModel("acc2", "Rekening Utama", "bank_account", BigDecimal("10000000"))
    val dummyAccounts = listOf(dummyAccount1, dummyAccount2)

    val dummyCategory1 =
        CategoryModel("cat1", "Makan & Minum", "restaurant", TransactionType.EXPENSE)
    val dummyCategory2 = CategoryModel("cat2", "Gaji", "salary", TransactionType.INCOME)

    val dummyTransactions = listOf(
        TransactionModel(
            "1",
            BigDecimal("50000"),
            TransactionType.EXPENSE,
            LocalDateTime.now(),
            "Kopi Pagi",
            dummyCategory1,
            dummyAccount1
        ),
        TransactionModel(
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
        BudgetModel(
            "b1",
            "Makan & Minum",
            dummyCategory1.name,
            dummyCategory1.iconIdentifier,
            BigDecimal("2000000"),
            BigDecimal("1250000"),
            YearMonth.now()
        ),
        BudgetModel(
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