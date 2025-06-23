package com.rifqi.trackfunds.core.data.di

import com.rifqi.trackfunds.core.data.repository.AccountRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.BudgetRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.CategoryRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.TransactionRepositoryImpl
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.BudgetRepository
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
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
        categoryRepositoryImpl: CategoryRepositoryImpl // Hilt tahu cara membuat ini karena constructornya @Inject
    ): CategoryRepository // Saat CategoryRepository diminta, Hilt akan menyediakan CategoryRepositoryImpl

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
}