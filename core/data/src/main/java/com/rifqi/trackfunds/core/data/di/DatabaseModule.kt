package com.rifqi.trackfunds.core.data.di

import android.content.Context
import com.rifqi.trackfunds.core.data.local.AppDatabase
import com.rifqi.trackfunds.core.data.local.dao.CategoryDao
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
        return AppDatabase.getDatabase(appContext /*, scope */)
    }

    @Provides
    @Singleton // DAO biasanya juga singleton karena databasenya singleton
    fun provideCategoryDao(appDatabase: AppDatabase): CategoryDao {
        return appDatabase.categoryDao()
    }

    // (Opsional) Menyediakan CoroutineScope level aplikasi jika dibutuhkan di tempat lain
    // Jika AppDatabase.getDatabase() Anda tidak lagi menerima scope, ini tidak wajib untuk database
    // @Provides
    // @Singleton
    // fun provideApplicationScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    // Tambahkan @Provides untuk DAO lain di sini nanti, misalnya:
    // @Provides
    // @Singleton
    // fun provideTransactionDao(appDatabase: AppDatabase): TransactionDao {
    //     return appDatabase.transactionDao()
    // }
}