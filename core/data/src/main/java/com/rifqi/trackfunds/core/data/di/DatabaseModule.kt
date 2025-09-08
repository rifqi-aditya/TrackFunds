package com.rifqi.trackfunds.core.data.di

import android.content.Context
import androidx.room.Room
import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.local.dao.BudgetDao
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
import com.rifqi.trackfunds.core.data.local.dao.SavingsGoalDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionItemDao
import com.rifqi.trackfunds.core.data.local.dao.UserDao
import com.rifqi.trackfunds.core.data.local.db.DatabaseSeeder
import com.rifqi.trackfunds.core.data.local.db.DbConfig
import com.rifqi.trackfunds.core.data.local.db.RoomTransactionRunner
import com.rifqi.trackfunds.core.data.local.db.TrackFundsDatabase
import com.rifqi.trackfunds.core.domain.transaction.TransactionRunner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /** Sediakan instance Room (tanpa singleton manual). */
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
        seeder: DatabaseSeeder,
        @ApplicationScope appScope: CoroutineScope
    ): TrackFundsDatabase {
        val db = Room.databaseBuilder(
            context,
            TrackFundsDatabase::class.java,
            DbConfig.NAME
        )
            // .addMigrations(MIGRATION_1_2, ...) // tambahkan jika sudah ada
            // .fallbackToDestructiveMigration()   // opsi sementara saat dev
            .build()

        // Seeding idempotent (akan insert hanya jika tabel masih kosong).
        appScope.launch { seeder.seedOnCreate(db) }

        return db
    }

    /** Transaction runner (pembungkus db.withTransaction). */
    @Provides
    @Singleton
    fun provideTransactionRunner(db: TrackFundsDatabase): TransactionRunner =
        RoomTransactionRunner(db)

    // ===== DAO providers (opsional; boleh langsung ambil via db di repository) =====
    @Provides
    @Singleton
    fun provideUserDao(db: TrackFundsDatabase): UserDao = db.userDao()
    @Provides
    @Singleton
    fun provideCategoryDao(db: TrackFundsDatabase): CategoryDao = db.categoryDao()
    @Provides
    @Singleton
    fun provideAccountDao(db: TrackFundsDatabase): AccountDao = db.accountDao()
    @Provides
    @Singleton
    fun provideTransactionDao(db: TrackFundsDatabase): TransactionDao = db.transactionDao()
    @Provides
    @Singleton
    fun provideTransactionItemDao(db: TrackFundsDatabase): TransactionItemDao =
        db.transactionItemDao()

    @Provides
    @Singleton
    fun provideBudgetDao(db: TrackFundsDatabase): BudgetDao = db.budgetDao()
    @Provides
    @Singleton
    fun provideSavingsGoalDao(db: TrackFundsDatabase): SavingsGoalDao = db.savingsGoalDao()
}