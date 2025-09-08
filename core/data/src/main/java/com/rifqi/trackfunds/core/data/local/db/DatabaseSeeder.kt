package com.rifqi.trackfunds.core.data.local.db

import androidx.room.withTransaction
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.category.model.TransactionType
import java.util.UUID
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

    // Gunakan data class kecil untuk membuat kode lebih mudah dibaca daripada List<Any>
    private data class CategorySeedData(
        val key: String,
        val icon: String,
        val type: TransactionType
    )

    private fun buildInitialCategories(): List<CategoryEntity> {
        val expense = listOf(
            CategorySeedData("category_food_and_drink", "food_and_drink", TransactionType.EXPENSE),
            CategorySeedData("category_groceries", "groceries", TransactionType.EXPENSE),
            CategorySeedData("category_transportation", "transportation", TransactionType.EXPENSE),
            CategorySeedData("category_fuel", "fuel", TransactionType.EXPENSE),
            CategorySeedData("category_shopping", "shopping", TransactionType.EXPENSE),
            CategorySeedData("category_utilities", "utilities", TransactionType.EXPENSE),
            CategorySeedData("category_housing", "housing", TransactionType.EXPENSE),
            CategorySeedData("category_health", "health", TransactionType.EXPENSE),
            CategorySeedData("category_insurance", "insurance", TransactionType.EXPENSE),
            CategorySeedData("category_personal_care", "personal_care", TransactionType.EXPENSE),
            CategorySeedData("category_entertainment", "entertainment", TransactionType.EXPENSE),
            CategorySeedData("category_education", "education", TransactionType.EXPENSE),
            CategorySeedData("category_travel", "travel", TransactionType.EXPENSE),
            CategorySeedData("category_sports_fitness", "sports_fitness", TransactionType.EXPENSE),
            CategorySeedData("category_pets", "pets", TransactionType.EXPENSE),
            CategorySeedData(
                "category_gifts_donations",
                "gifts_donations",
                TransactionType.EXPENSE
            ),
            CategorySeedData("category_fees_charges", "fees_charges", TransactionType.EXPENSE),
            CategorySeedData("category_miscellaneous", "miscellaneous", TransactionType.EXPENSE),
        )

        val income = listOf(
            CategorySeedData("category_salary", "salary", TransactionType.INCOME),
            CategorySeedData("category_bonus", "bonus", TransactionType.INCOME),
            CategorySeedData(
                "category_investment_income",
                "investment_income",
                TransactionType.INCOME
            ),
            CategorySeedData("category_gifts_received", "gifts_received", TransactionType.INCOME),
            CategorySeedData("category_freelance", "freelance", TransactionType.INCOME),
            CategorySeedData("category_other_income", "other_income", TransactionType.INCOME),
        )

        return (expense + income).map { seedData ->
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = seedData.key,
                iconIdentifier = seedData.icon,
                type = seedData.type,
                standardKey = seedData.key
            )
        }
    }
}