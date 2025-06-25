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
            // --- 1. MEMBUAT KATEGORI DEFAULT DENGAN STANDARD KEY ---
            val initialCategories = listOf(
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Food & Drink",
                    iconIdentifier = "restaurant",
                    type = TransactionType.EXPENSE,
                    standardKey = "food_and_drink"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Transportation",
                    iconIdentifier = "commute",
                    type = TransactionType.EXPENSE,
                    standardKey = "transportation"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Bills",
                    iconIdentifier = "receipt_long",
                    type = TransactionType.EXPENSE,
                    standardKey = "utilities"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Shopping",
                    iconIdentifier = "shopping_bag",
                    type = TransactionType.EXPENSE,
                    standardKey = "shopping"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Entertainment",
                    iconIdentifier = "theaters",
                    type = TransactionType.EXPENSE,
                    standardKey = "entertainment"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Health",
                    iconIdentifier = "health_and_safety",
                    type = TransactionType.EXPENSE,
                    standardKey = "health"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Education",
                    iconIdentifier = "school",
                    type = TransactionType.EXPENSE,
                    standardKey = "education"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Salary",
                    iconIdentifier = "payments",
                    type = TransactionType.INCOME,
                    standardKey = "salary"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Bonus",
                    iconIdentifier = "emoji_events",
                    type = TransactionType.INCOME,
                    standardKey = "bonus"
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Investment",
                    iconIdentifier = "trending_up",
                    type = TransactionType.INCOME,
                    standardKey = "investment_income"
                ),
            )
            categoryDao.insertAllCategories(initialCategories)

            // --- 2. MEMBUAT AKUN DEFAULT DENGAN SALDO AKHIR YANG SUDAH DIHITUNG ---
            val initialAccounts = listOf(
                // Saldo awal 500rb, dikurangi 50rb untuk makan siang = 450rb
                AccountEntity(
                    id = "acc_cash",
                    name = "Cash Wallet",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("450000")
                ),
                // Saldo awal 10jt, ditambah gaji 15jt, dikurangi belanja 750rb = 24.25jt
                AccountEntity(
                    id = "acc_mbanking",
                    name = "MBanking",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("24250000")
                )
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
                    accountId = cashWalletAccount.id // Mengarah ke ID Cash Wallet
                )
            )

            transactionDao.insertAllTransaction(initialTransactions)
        }
    }
}