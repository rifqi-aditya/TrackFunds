package com.rifqi.trackfunds.core.data.di

import android.content.Context
import com.rifqi.trackfunds.core.data.local.AppDatabase
import com.rifqi.trackfunds.core.data.local.RoomTransactionRunner
import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.local.dao.BudgetDao
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.dao.SavingsGoalDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionItemDao
import com.rifqi.trackfunds.core.data.local.dao.UserDao
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Menginstal modul ini di cakupan Singleton (seluruh aplikasi)
object DatabaseModule {

    @Provides
    @Singleton // Memastikan hanya ada satu instance AppDatabase di seluruh aplikasi
    fun provideAppDatabase(
        @ApplicationContext appContext: Context,
        // Menyediakan CoroutineScope untuk callback database jika diperlukan,
        // atau bisa juga dibuat langsung di AppDatabase.getDatabase
        // Jika AppDatabase.getDatabase sudah handle scope sendiri, ini bisa dihilangkan.
        // scope: CoroutineScope -> dari provideApplicationScope (lihat di bawah)
    ): AppDatabase {
        // Menggunakan metode getDatabase yang sudah kita buat di AppDatabase
        // Jika AppDatabase.getDatabase Anda menerima scope, Anda perlu menyediakannya
        // Untuk versi AppDatabase.kt yang saya berikan sebelumnya, ia membuat scope default.
        return AppDatabase.getDatabase(appContext)
    }

    @Provides
    @Singleton
    fun provideTransactionRunner(db: AppDatabase): AppTransactionRunner {
        return RoomTransactionRunner(db)
    }

    @Provides
    @Singleton // DAO biasanya juga singleton karena databasenya singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    @Provides
    @Singleton
    fun provideAccountDao(appDatabase: AppDatabase): AccountDao {
        return appDatabase.accountDao()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
        return appDatabase.transactionDao()
    }

    @Provides
    @Singleton
    fun provideTransactionItemDao(appDatabase: AppDatabase): TransactionItemDao {
        return appDatabase.transactionItemDao()
    }

    @Provides
    @Singleton
    fun provideBudgetDao(appDatabase: AppDatabase): BudgetDao {
        return appDatabase.budgetDao()
    }

    @Provides
    @Singleton
    fun provideSavingsGoalDao(appDatabase: AppDatabase): SavingsGoalDao {
        return appDatabase.savingsGoalDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: AppDatabase): UserDao { // <-- Tambahkan ini
        return database.userDao()
    }



    // (Opsional) Menyediakan CoroutineScope level aplikasi jika dibutuhkan di tempat lain
    // Jika AppDatabase.getDatabase() Anda tidak lagi menerima scope, ini tidak wajib untuk database
    // @Provides
    // @Singleton
    // fun provideApplicationScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)
}