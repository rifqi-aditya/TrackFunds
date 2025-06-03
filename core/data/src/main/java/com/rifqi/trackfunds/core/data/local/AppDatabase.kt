package com.rifqi.trackfunds.core.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.entity.CategoryEntity
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

@Database(
    entities = [CategoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao

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
                    populateDatabase(database.categoryDao())
                }
            }
        }

        suspend fun populateDatabase(categoryDao: CategoryDao) {
            val initialCategories = listOf(
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Makanan & Minuman", iconIdentifier = "restaurant", type = TransactionType.EXPENSE.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Transportasi", iconIdentifier = "car", type = TransactionType.EXPENSE.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Tagihan", iconIdentifier = "apartment", type = TransactionType.EXPENSE.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Belanja", iconIdentifier = "shopping_cart", type = TransactionType.EXPENSE.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Hiburan", iconIdentifier = "movie", type = TransactionType.EXPENSE.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Kesehatan", iconIdentifier = "medical_doctor", type = TransactionType.EXPENSE.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Pendidikan", iconIdentifier = "school", type = TransactionType.EXPENSE.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Gaji", iconIdentifier = "cash", type = TransactionType.INCOME.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Bonus", iconIdentifier = "growing_money", type = TransactionType.INCOME.toString()),
                CategoryEntity(id = UUID.randomUUID().toString(), name = "Investasi", iconIdentifier = "stack_of_money", type = TransactionType.INCOME.toString()),
            )
            categoryDao.insertAllCategories(initialCategories)
        }
    }
}