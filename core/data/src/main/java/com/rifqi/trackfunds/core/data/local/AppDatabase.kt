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
import com.rifqi.trackfunds.core.data.local.entity.AccountEntity
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.UUID

@Database(
    entities = [CategoryEntity::class, AccountEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun accountDao(): AccountDao

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
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.categoryDao(), database.accountDao())
                }
            }
        }

        suspend fun populateDatabase(categoryDao: CategoryDao, accountDao: AccountDao) {
            val initialCategories = listOf(
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Makanan & Minuman",
                    iconIdentifier = "restaurant",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Transportasi",
                    iconIdentifier = "car",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Tagihan",
                    iconIdentifier = "apartment",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Belanja",
                    iconIdentifier = "shopping_cart",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Hiburan",
                    iconIdentifier = "movie",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Kesehatan",
                    iconIdentifier = "medical_doctor",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Pendidikan",
                    iconIdentifier = "school",
                    type = TransactionType.EXPENSE
                ),
                CategoryEntity(
                    id = UUID.randomUUID().toString(),
                    name = "Gaji",
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
                    name = "Investasi",
                    iconIdentifier = "stack_of_money",
                    type = TransactionType.INCOME
                ),
            )
            categoryDao.insertAllCategories(initialCategories)

            val initialAccounts = listOf(
                AccountEntity(
                    id = "acc1",
                    name = "Cash Wallet",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("1250000.0")
                ),
                AccountEntity(
                    id = "acc2",
                    name = "Mbanking BCA",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("1250000.0")
                ),
                AccountEntity(
                    id = "acc3",
                    name = "GoPay",
                    iconIdentifier = "wallet_account",
                    balance = BigDecimal("1250000.0")
                )
            )
            accountDao.insertAll(initialAccounts)
        }
    }
}