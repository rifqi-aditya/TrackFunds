package com.rifqi.trackfunds.di

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
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByTypeUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByTypeUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) // Karena Use Case akan di-inject ke ViewModel
abstract class UseCaseModule {

    // --- Category Use Cases ---
    @Binds
    @ViewModelScoped
    abstract fun bindGetCategoriesUseCase(
        useCaseImpl: GetCategoriesUseCaseImpl
    ): GetCategoriesUseCase

    // --- Account Use Cases ---
    @Binds
    @ViewModelScoped
    abstract fun bindGetAccountsUseCase(
        useCaseImpl: GetAccountsUseCaseImpl
    ): GetAccountsUseCase

//    @Binds
//    @ViewModelScoped
//    abstract fun bindGetAccountDetailsUseCase(
//        useCaseImpl: GetAccountDetailsUseCaseImpl
//    ): GetAccountDetailsUseCase

    // --- Transaction Use Cases ---
    @Binds
    @ViewModelScoped
    abstract fun bindAddTransactionUseCase(
        useCaseImpl: AddTransactionUseCaseImpl
    ): AddTransactionUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetTransactionsUseCase(
        useCaseImpl: GetTransactionsUseCaseImpl
    ): GetTransactionsUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetTransactionsByTypeUseCase(
        useCaseImpl: GetTransactionsByTypeUseCaseImpl
    ): GetTransactionsByTypeUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindGetCategorySummariesUseCase(
        useCaseImpl: GetCategorySummariesUseCaseImpl
    ): GetCategorySummariesUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateTransactionUseCase(
        useCaseImpl: UpdateTransactionUseCaseImpl
    ): UpdateTransactionUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteTransactionUseCase(
        useCaseImpl: DeleteTransactionUseCaseImpl
    ): DeleteTransactionUseCase

}