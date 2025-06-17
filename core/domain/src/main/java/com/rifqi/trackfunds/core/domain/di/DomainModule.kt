package com.rifqi.trackfunds.core.domain.di

import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetCategorySummariesUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetCategorySummariesUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByCategoryIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByCategoryIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByDateRangeUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByDateRangeUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByTypeUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByTypeUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    // --- Category Use Cases ---
    @Binds
    @Singleton
    abstract fun bindGetCategoriesUseCase(
        useCaseImpl: GetCategoriesUseCaseImpl
    ): GetCategoriesUseCase

    // --- Account Use Cases ---
    @Binds
    @Singleton
    abstract fun bindGetAccountsUseCase(
        useCaseImpl: GetAccountsUseCaseImpl
    ): GetAccountsUseCase

//    @Binds
//    @Singleton
//    abstract fun bindGetAccountDetailsUseCase(
//        useCaseImpl: GetAccountDetailsUseCaseImpl
//    ): GetAccountDetailsUseCase

    // --- Transaction Use Cases ---
    @Binds
    @Singleton
    abstract fun bindAddTransactionUseCase(
        useCaseImpl: AddTransactionUseCaseImpl
    ): AddTransactionUseCase

    @Binds
    @Singleton
    abstract fun bindGetTransactionsUseCase(
        useCaseImpl: GetTransactionsUseCaseImpl
    ): GetTransactionsUseCase


    @Binds
    @Singleton
    abstract fun bindGetCategorySummariesUseCase(
        useCaseImpl: GetCategorySummariesUseCaseImpl
    ): GetCategorySummariesUseCase

    @Binds
    @Singleton
    abstract fun bindGetTransactionsByDateRangeUseCase(
        useCaseImpl: GetTransactionsByDateRangeUseCaseImpl
    ): GetTransactionsByDateRangeUseCase

    @Binds
    @Singleton
    abstract fun bindGetTransactionsByTypeUseCase(
        useCaseImpl: GetTransactionsByTypeUseCaseImpl
    ): GetTransactionsByTypeUseCase

    @Binds
    @Singleton
    abstract fun bindGetTransactionsByCategoryIdUseCase(
        getTransactionsByCategoryIdUseCaseImpl: GetTransactionsByCategoryIdUseCaseImpl
    ): GetTransactionsByCategoryIdUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateTransactionUseCase(
        useCaseImpl: UpdateTransactionUseCaseImpl
    ): UpdateTransactionUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteTransactionUseCase(
        useCaseImpl: DeleteTransactionUseCaseImpl
    ): DeleteTransactionUseCase
}