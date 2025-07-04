package com.rifqi.trackfunds.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rifqi.trackfunds.core.data.local.converter.Converters
import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.local.dao.BudgetDao
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.local.entity.AccountEntity
import com.rifqi.trackfunds.core.data.local.entity.BudgetEntity
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Database(
    entities = [
        CategoryEntity::class,
        AccountEntity::class,
        TransactionEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "trackfunds_app_database"
                )
                    .fallbackToDestructiveMigration() // Hapus ini di produksi!
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(
                        database.categoryDao(),
                        database.accountDao(),
                        database.transactionDao()
                    )
                }
            }
        }

        suspend fun populateDatabase(
            categoryDao: CategoryDao,
            accountDao: AccountDao,
            transactionDao: TransactionDao
        ) {
            // --- MEMBUAT KATEGORI DEFAULT DENGAN STANDARD KEY & ICON IDENTIFIER YANG SAMA ---
            val initialCategories = listOf(
                // --- KATEGORI PENGELUARAN (EXPENSE) ---
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Food & Drink",
                    iconIdentifier = "food_and_drink",
                    type = TransactionType.EXPENSE,
                    standardKey = "food_and_drink"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Groceries",
                    iconIdentifier = "groceries",
                    type = TransactionType.EXPENSE,
                    standardKey = "groceries"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Transportation",
                    iconIdentifier = "transportation",
                    type = TransactionType.EXPENSE,
                    standardKey = "transportation"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Fuel",
                    iconIdentifier = "fuel",
                    type = TransactionType.EXPENSE,
                    standardKey = "fuel"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Shopping",
                    iconIdentifier = "shopping",
                    type = TransactionType.EXPENSE,
                    standardKey = "shopping"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Bills & Utilities",
                    iconIdentifier = "utilities",
                    type = TransactionType.EXPENSE,
                    standardKey = "utilities"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Housing",
                    iconIdentifier = "housing",
                    type = TransactionType.EXPENSE,
                    standardKey = "housing"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Health",
                    iconIdentifier = "health",
                    type = TransactionType.EXPENSE,
                    standardKey = "health"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Insurance",
                    iconIdentifier = "insurance",
                    type = TransactionType.EXPENSE,
                    standardKey = "insurance"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Personal Care",
                    iconIdentifier = "personal_care",
                    type = TransactionType.EXPENSE,
                    standardKey = "personal_care"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Entertainment",
                    iconIdentifier = "entertainment",
                    type = TransactionType.EXPENSE,
                    standardKey = "entertainment"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Education",
                    iconIdentifier = "education",
                    type = TransactionType.EXPENSE,
                    standardKey = "education"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Holidays & Travel",
                    iconIdentifier = "travel",
                    type = TransactionType.EXPENSE,
                    standardKey = "travel"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Sports & Fitness",
                    iconIdentifier = "sports_fitness",
                    type = TransactionType.EXPENSE,
                    standardKey = "sports_fitness"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Pets",
                    iconIdentifier = "pets",
                    type = TransactionType.EXPENSE,
                    standardKey = "pets"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Gifts & Donations",
                    iconIdentifier = "gifts_donations",
                    type = TransactionType.EXPENSE,
                    standardKey = "gifts_donations"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Fees & Charges",
                    iconIdentifier = "fees_charges",
                    type = TransactionType.EXPENSE,
                    standardKey = "fees_charges"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Miscellaneous",
                    iconIdentifier = "miscellaneous",
                    type = TransactionType.EXPENSE,
                    standardKey = "miscellaneous"
                ),

                // --- KATEGORI PEMASUKAN (INCOME) ---
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Salary",
                    iconIdentifier = "salary",
                    type = TransactionType.INCOME,
                    standardKey = "salary"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Bonus",
                    iconIdentifier = "bonus",
                    type = TransactionType.INCOME,
                    standardKey = "bonus"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Investment",
                    iconIdentifier = "investment_income",
                    type = TransactionType.INCOME,
                    standardKey = "investment_income"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Gifts",
                    iconIdentifier = "gifts_received",
                    type = TransactionType.INCOME,
                    standardKey = "gifts_received"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Freelance",
                    iconIdentifier = "freelance",
                    type = TransactionType.INCOME,
                    standardKey = "freelance"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Other Income",
                    iconIdentifier = "other_income",
                    type = TransactionType.INCOME,
                    standardKey = "other_income"
                )
            )
            categoryDao.insertAllCategories(initialCategories)

            // --- MEMBUAT AKUN DEFAULT ---
            val initialAccounts = listOf(
                AccountEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Cash Wallet",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("500000")
                ),
                AccountEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Main Bank Account",
                    iconIdentifier = "bank_account",
                    balance = BigDecimal("10000000")
                ),
            )
            accountDao.insertAll(initialAccounts)

            // --- 3. MEMBUAT TRANSAKSI DUMMY DENGAN FOREIGN KEY YANG BENAR ---

            // Mengambil ID dari list di atas untuk memastikan foreign key valid
            val salaryCategory = initialCategories.find { it.name == "Salary" }!!
            val shoppingCategory = initialCategories.find { it.name == "Shopping" }!!
            val foodCategory = initialCategories.find { it.name == "Food & Drink" }!!

            val cashWalletAccount = initialAccounts.find { it.name == "Cash Wallet" }!!
            val mbankingAccount =
                initialAccounts.find { it.name == "MBanking" }!!

            // Daftar transaksi dummy sekarang diaktifkan
            val initialTransactions = listOf(
                TransactionEntity(
                    id = UUID.randomUUID().toString(),
                    note = "Monthly Salary",
                    amount = BigDecimal("15000000"),
                    type = TransactionType.INCOME,
                    date = LocalDateTime.now().minusDays(5),
                    categoryId = salaryCategory.id,
                    accountId = mbankingAccount.id // Mengarah ke ID MBanking
                ),
                TransactionEntity(
                    id = UUID.randomUUID().toString(),
                    note = "Monthly Shopping",
                    amount = BigDecimal("750000"),
                    type = TransactionType.EXPENSE,
                    date = LocalDateTime.now().minusDays(4),
                    categoryId = shoppingCategory.id,
                    accountId = mbankingAccount.id // Mengarah ke ID MBanking
                ),
                TransactionEntity(
                    id = UUID.randomUUID().toString(),
                    note = "Lunch",
                    amount = BigDecimal("50000"),
                    type = TransactionType.EXPENSE,
                    date = LocalDateTime.now().minusDays(2),
                    categoryId = foodCategory.id,
                    accountId = cashWalletAccount.id
                )
            )
            transactionDao.insertAllTransaction(initialTransactions)
        }
    }
}