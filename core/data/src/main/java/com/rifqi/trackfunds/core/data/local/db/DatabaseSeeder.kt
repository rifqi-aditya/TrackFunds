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
        // 1. Tambahkan parameter 'englishName' ke data class
        data class CategorySeedData(
            val key: String,
            val englishName: String,
            val icon: String,
            val type: TransactionType
        )

        val expense = listOf(
            CategorySeedData("category_food_and_drink", "Food & Drink", "food_and_drink", TransactionType.EXPENSE),
            CategorySeedData("category_groceries", "Groceries", "groceries", TransactionType.EXPENSE),
            CategorySeedData("category_transportation", "Transportation", "transportation", TransactionType.EXPENSE),
            CategorySeedData("category_fuel", "Fuel", "fuel", TransactionType.EXPENSE),
            CategorySeedData("category_shopping", "Shopping", "shopping", TransactionType.EXPENSE),
            CategorySeedData("category_utilities", "Bills & Utilities", "utilities", TransactionType.EXPENSE),
            CategorySeedData("category_housing", "Housing", "housing", TransactionType.EXPENSE),
            CategorySeedData("category_health", "Health", "health", TransactionType.EXPENSE),
            CategorySeedData("category_insurance", "Insurance", "insurance", TransactionType.EXPENSE),
            CategorySeedData("category_personal_care", "Personal Care", "personal_care", TransactionType.EXPENSE),
            CategorySeedData("category_entertainment", "Entertainment", "entertainment", TransactionType.EXPENSE),
            CategorySeedData("category_education", "Education", "education", TransactionType.EXPENSE),
            CategorySeedData("category_travel", "Holidays & Travel", "travel", TransactionType.EXPENSE),
            CategorySeedData("category_sports_fitness", "Sports & Fitness", "sports_fitness", TransactionType.EXPENSE),
            CategorySeedData("category_pets", "Pets", "pets", TransactionType.EXPENSE),
            CategorySeedData("category_gifts_donations", "Gifts & Donations", "gifts_donations", TransactionType.EXPENSE),
            CategorySeedData("category_fees_charges", "Fees & Charges", "fees_charges", TransactionType.EXPENSE),
            CategorySeedData("category_miscellaneous", "Miscellaneous", "miscellaneous", TransactionType.EXPENSE),
        )

        val income = listOf(
            CategorySeedData("category_salary", "Salary", "salary", TransactionType.INCOME),
            CategorySeedData("category_bonus", "Bonus", "bonus", TransactionType.INCOME),
            CategorySeedData("category_investment_income", "Investment Income", "investment_income", TransactionType.INCOME),
            CategorySeedData("category_gifts_received", "Gifts Received", "gifts_received", TransactionType.INCOME),
            CategorySeedData("category_freelance", "Freelance", "freelance", TransactionType.INCOME),
            CategorySeedData("category_other_income", "Other Income", "other_income", TransactionType.INCOME),
        )

        return (expense + income).map { seedData ->
            CategoryEntity(
                id = UUID.randomUUID().toString(),
                name = seedData.englishName, // <-- 2. Gunakan nama bahasa Inggris untuk kolom 'name'
                iconIdentifier = seedData.icon,
                type = seedData.type,
                standardKey = seedData.key      // <-- Kolom 'standardKey' tetap menggunakan kunci
            )
        }
    }
}