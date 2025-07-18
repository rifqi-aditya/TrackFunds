package com.rifqi.trackfunds.core.data.di

import com.rifqi.trackfunds.core.data.repository.AccountRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.BudgetRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.CategoryRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.SavingsRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.ScanRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.TransactionRepositoryImpl
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import com.rifqi.trackfunds.core.domain.repository.ScanRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        budgetRepositoryImpl: BudgetRepositoryImpl
    ): BudgetRepository

    @Binds
    @Singleton
    abstract fun bindScanRepository(
        scanRepositoryImpl: ScanRepositoryImpl
    ): ScanRepository

    @Binds
    @Singleton
    abstract fun bindSavingsRepository(
        savingsRepositoryImpl: SavingsRepositoryImpl
    ): SavingsRepository
}