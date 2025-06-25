package com.rifqi.trackfunds.core.domain.di

import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.AddBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.AddBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.DeleteBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.DeleteBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCase
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetCategorySummariesUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetCategorySummariesUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByCategoryIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByCategoryIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByDateRangeUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByDateRangeUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByTypeUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsByTypeUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCaseImpl
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

    @Binds
    @Singleton
    abstract fun bindGetCategoryUseCase(
        useCaseImpl: GetCategoryUseCaseImpl
    ): GetCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoryByStandardKeyUseCase(
        useCaseImpl: GetCategoryByStandardKeyUseCaseImpl
    ): GetCategoryByStandardKeyUseCase

    // --- Account Use Cases ---
    @Binds
    @Singleton
    abstract fun bindGetAccountsUseCase(
        useCaseImpl: GetAccountsUseCaseImpl
    ): GetAccountsUseCase

    @Binds
    @Singleton
    abstract fun bindGetAccountUseCase(
        useCaseImpl: GetAccountUseCaseImpl
    ): GetAccountUseCase

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
    abstract fun bindGetTransactionByIdUseCase(
        useCaseImpl: GetTransactionByIdUseCaseImpl
    ): GetTransactionByIdUseCase

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

    @Binds
    @Singleton
    abstract fun bindPerformTransferUseCase(
        useCaseImpl: PerformTransferUseCaseImpl
    ): PerformTransferUseCase

    // --- Budget Use Case ---
    @Binds
    @Singleton
    abstract fun bindGetBudgetsUseCase(
        useCaseImpl: GetBudgetsUseCaseImpl
    ): GetBudgetsUseCase

    @Binds
    @Singleton
    abstract fun bindGetBudgetByIdUseCase(
        useCaseImpl: GetBudgetByIdUseCaseImpl
    ): GetBudgetByIdUseCase

    @Binds
    @Singleton
    abstract fun bindAddBudgetUseCase(
        useCaseImpl: AddBudgetUseCaseImpl
    ): AddBudgetUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateBudgetUseCase(
        useCaseImpl: UpdateBudgetUseCaseImpl
    ): UpdateBudgetUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteBudgetUseCase(
        useCaseImpl: DeleteBudgetUseCaseImpl
    ): DeleteBudgetUseCase

    // --- Scan Use Case ---
    @Binds
    @Singleton
    abstract fun bindScanReceiptUseCase(
        useCaseImpl: ScanReceiptUseCaseImpl
    ): ScanReceiptUseCase
}