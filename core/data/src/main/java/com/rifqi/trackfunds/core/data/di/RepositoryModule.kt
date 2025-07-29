package com.rifqi.trackfunds.core.data.di

import com.rifqi.trackfunds.core.data.repository.AccountRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.AuthRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.BudgetRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.CategoryRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.SavingsGoalRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.ScanRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.TransactionRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.UserPreferencesRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.UserRepositoryImpl
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.AuthRepository
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.ScanRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.repository.UserRepository
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
        savingsRepositoryImpl: SavingsGoalRepositoryImpl
    ): SavingsGoalRepository

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        impl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository
}