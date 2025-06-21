package com.rifqi.trackfunds.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rifqi.trackfunds.core.data.local.converter.Converters
import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.local.entity.AccountEntity
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID

@Database(
    entities = [
        CategoryEntity::class,
        AccountEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

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
            val initialCategories = listOf(
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Food & Drink",
                    iconIdentifier = "restaurant",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Transportation",
                    iconIdentifier = "car",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Bills",
                    iconIdentifier = "apartment",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Shopping",
                    iconIdentifier = "shopping_cart",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Entertainment",
                    iconIdentifier = "movie",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Health",
                    iconIdentifier = "medical_doctor",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Education",
                    iconIdentifier = "school",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Salary",
                    iconIdentifier = "cash",
                    type = TransactionType.INCOME
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Bonus",
                    iconIdentifier = "growing_money",
                    type = TransactionType.INCOME
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Investment",
                    iconIdentifier = "stack_of_money",
                    type = TransactionType.INCOME
                ),
            )
            categoryDao.insertAllCategories(initialCategories)

            val initialAccounts = listOf(
                AccountEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Cash Wallet",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("0")
                ),
                AccountEntity(
                    id = UUID.randomUUID().toString(),
                    name = "MBanking",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("10000000")
                ),
            )
            accountDao.insertAll(initialAccounts)

            val salaryCategory = initialCategories.find { it.name == "Salary" }!!
            val shoppingCategory = initialCategories.find { it.name == "Shopping" }!!
            val foodCategory = initialCategories.find { it.name == "Food & Drink" }!!

            val cashWalletAccount = initialAccounts.find { it.name == "Cash Wallet" }!!
            val bcaAccount = initialAccounts.find { it.name == "BCA Mobile" }!!

//            val initialTransactions = listOf(
//                TransactionEntity(
//                    id = UUID.randomUUID().toString(),
//                    note = "Monthly Salary",
//                    amount = BigDecimal("15000000"),
//                    type = TransactionType.INCOME,
//                    date = LocalDateTime.now().minusDays(5),
//                    categoryId = salaryCategory.id,
//                    accountId = bcaAccount.id
//                ),
//                TransactionEntity(
//                    id = UUID.randomUUID().toString(),
//                    note = "Monthly Shopping",
//                    amount = BigDecimal("750000"),
//                    type = TransactionType.EXPENSE,
//                    date = LocalDateTime.now().minusDays(4),
//                    categoryId = shoppingCategory.id,
//                    accountId = bcaAccount.id
//                ),
//                TransactionEntity(
//                    id = UUID.randomUUID().toString(),
//                    note = "Lunch",
//                    amount = BigDecimal("50000"),
//                    type = TransactionType.EXPENSE,
//                    date = LocalDateTime.now().minusDays(2),
//                    categoryId = foodCategory.id,
//                    accountId = cashWalletAccount.id
//                )
//            )
//
//            transactionDao.insertAllTransaction(initialTransactions)
        }
    }
}