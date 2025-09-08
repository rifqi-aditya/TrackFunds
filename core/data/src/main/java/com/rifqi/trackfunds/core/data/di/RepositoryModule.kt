package com.rifqi.trackfunds.core.data.di

import com.rifqi.trackfunds.core.data.repository.AccountRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.AppPrefsRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.AuthRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.BudgetRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.CategoryRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.SavingsGoalRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.ScanRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.SettingsRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.TransactionRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.UserRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.UserSessionRepositoryImpl
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.auth.repository.AuthRepository
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.budget.repository.BudgetRepository
import com.rifqi.trackfunds.core.domain.category.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.common.repository.AppPrefsRepository
import com.rifqi.trackfunds.core.domain.savings.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.scan.repository.ScanRepository
import com.rifqi.trackfunds.core.domain.settings.repository.SettingsRepository
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.user.repository.UserRepository
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
    abstract fun bindAuthRepository(
        impl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserSessionRepository(
        impl: UserSessionRepositoryImpl
    ): UserSessionRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindAppPrefsRepository(
        impl: AppPrefsRepositoryImpl
    ): AppPrefsRepository
}