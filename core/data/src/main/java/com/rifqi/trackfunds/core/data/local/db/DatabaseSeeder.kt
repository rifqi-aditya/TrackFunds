package com.rifqi.trackfunds.core.data.local.db

import androidx.room.withTransaction
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseSeeder @Inject constructor() {

    /**
     * Jalankan hanya saat DB dibuat pertama kali.
     * Idempotent: akan insert bila tabel kategori masih kosong.
     */
    suspend fun seedOnCreate(db: TrackFundsDatabase) {
        db.withTransaction {
            val categoryDao = db.categoryDao()
            if (categoryDao.count() > 0) return@withTransaction

            val initial = buildInitialCategories()
            categoryDao.insertAllCategories(initial)
        }
    }

    private fun buildInitialCategories(): List<CategoryEntity> {
        val expense = listOf(
            listOf("Food & Drink", "food_and_drink", TransactionType.EXPENSE, "food_and_drink"),
            listOf("Groceries", "groceries", TransactionType.EXPENSE, "groceries"),
            listOf("Transportation", "transportation", TransactionType.EXPENSE, "transportation"),
            listOf("Fuel", "fuel", TransactionType.EXPENSE, "fuel"),
            listOf("Shopping", "shopping", TransactionType.EXPENSE, "shopping"),
            listOf("Bills & Utilities", "utilities", TransactionType.EXPENSE, "utilities"),
            listOf("Housing", "housing", TransactionType.EXPENSE, "housing"),
            listOf("Health", "health", TransactionType.EXPENSE, "health"),
            listOf("Insurance", "insurance", TransactionType.EXPENSE, "insurance"),
            listOf("Personal Care", "personal_care", TransactionType.EXPENSE, "personal_care"),
            listOf("Entertainment", "entertainment", TransactionType.EXPENSE, "entertainment"),
            listOf("Education", "education", TransactionType.EXPENSE, "education"),
            listOf("Holidays & Travel", "travel", TransactionType.EXPENSE, "travel"),
            listOf("Sports & Fitness", "sports_fitness", TransactionType.EXPENSE, "sports_fitness"),
            listOf("Pets", "pets", TransactionType.EXPENSE, "pets"),
            listOf(
                "Gifts & Donations",
                "gifts_donations",
                TransactionType.EXPENSE,
                "gifts_donations"
            ),
            listOf("Fees & Charges", "fees_charges", TransactionType.EXPENSE, "fees_charges"),
            listOf("Miscellaneous", "miscellaneous", TransactionType.EXPENSE, "miscellaneous"),
        )

        val income = listOf(
            listOf("Salary", "salary", TransactionType.INCOME, "salary"),
            listOf("Bonus", "bonus", TransactionType.INCOME, "bonus"),
            listOf("Investment", "investment_income", TransactionType.INCOME, "investment_income"),
            listOf("Gifts", "gifts_received", TransactionType.INCOME, "gifts_received"),
            listOf("Freelance", "freelance", TransactionType.INCOME, "freelance"),
            listOf("Other Income", "other_income", TransactionType.INCOME, "other_income"),
        )

        return (expense + income).map { (name, icon, type, key) ->
            CategoryEntity(
                id = key as String,
                name = name as String,
                iconIdentifier = icon as String,
                type = type as TransactionType,
                standardKey = key
            )
        }
    }
}