package com.rifqi.trackfunds.core.domain.di

import com.rifqi.trackfunds.core.domain.usecase.account.AddAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.AddAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.DeleteAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.DeleteAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsByIdsUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsByIdsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.UpdateAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.UpdateAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.auth.LoginUserUseCase
import com.rifqi.trackfunds.core.domain.usecase.auth.LoginUserUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.auth.LogoutUserUseCase
import com.rifqi.trackfunds.core.domain.usecase.auth.LogoutUserUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.auth.RegisterUserUseCase
import com.rifqi.trackfunds.core.domain.usecase.auth.RegisterUserUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.AddBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.AddBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.DeleteBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.DeleteBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetTopBudgetsUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetTopBudgetsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.AddCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.AddCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesByIdsUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesByIdsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.UpdateCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.UpdateCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.AddFundsToSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.AddFundsToSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.CreateSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.CreateSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.DeleteSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.DeleteSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.GetFilteredSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetFilteredSavingsGoalsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.GetSavingsGoalByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetSavingsGoalByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.SaveSavingsIconUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.SaveSavingsIconUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCase
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {
    // --- Category Use Cases ---
    @Binds
    @Singleton
    abstract fun bindGetFilteredCategoriesUseCase(
        useCaseImpl: GetFilteredCategoriesUseCaseImpl
    ): GetFilteredCategoriesUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoryUseCase(
        useCaseImpl: GetCategoryUseCaseImpl
    ): GetCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoriesByIdsUseCase(
        useCaseImpl: GetCategoriesByIdsUseCaseImpl
    ): GetCategoriesByIdsUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoryByStandardKeyUseCase(
        useCaseImpl: GetCategoryByStandardKeyUseCaseImpl
    ): GetCategoryByStandardKeyUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateCategoryUseCase(
        useCaseImpl: UpdateCategoryUseCaseImpl
    ): UpdateCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindAddCategoryUseCase(
        useCaseImpl: AddCategoryUseCaseImpl
    ): AddCategoryUseCase

    // --- Account Use Cases ---
    @Binds
    @Singleton
    abstract fun bindGetAccountsUseCase(
        useCaseImpl: GetAccountsUseCaseImpl
    ): GetAccountsUseCase

    @Binds
    @Singleton
    abstract fun getAccountsByIdsUseCase(
        useCaseImpl: GetAccountsByIdsUseCaseImpl
    ): GetAccountsByIdsUseCase

    @Binds
    @Singleton
    abstract fun bindAddAccountUseCase(
        useCaseImpl: AddAccountUseCaseImpl
    ): AddAccountUseCase

    @Binds
    @Singleton
    abstract fun bindUpdateAccountUseCase(
        useCaseImpl: UpdateAccountUseCaseImpl
    ): UpdateAccountUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteAccountUseCase(
        useCaseImpl: DeleteAccountUseCaseImpl
    ): DeleteAccountUseCase

    @Binds
    @Singleton
    abstract fun bindGetAccountByIdUseCase(
        useCaseImpl: GetAccountByIdUseCaseImpl
    ): GetAccountByIdUseCase

    // --- Transaction Use Cases ---
    @Binds
    @Singleton
    abstract fun bindAddTransactionUseCase(
        useCaseImpl: AddTransactionUseCaseImpl
    ): AddTransactionUseCase

    @Binds
    @Singleton
    abstract fun bindGetFilteredTransactionsUseCase(
        useCaseImpl: GetFilteredTransactionsUseCaseImpl
    ): GetFilteredTransactionsUseCase

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
    abstract fun bindGetTopBudgetsUseCase(
        useCaseImpl: GetTopBudgetsUseCaseImpl
    ): GetTopBudgetsUseCase

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


    // --- Savings Use Case ---
    @Binds
    @Singleton
    abstract fun bindGetActiveSavingsGoalsUseCase(
        useCaseImpl: GetActiveSavingsGoalsUseCaseImpl
    ): GetActiveSavingsGoalsUseCase

    @Binds
    @Singleton
    abstract fun bindGetFilteredSavingsGoalsUseCase(
        useCaseImpl: GetFilteredSavingsGoalsUseCaseImpl
    ): GetFilteredSavingsGoalsUseCase

    @Binds
    @Singleton
    abstract fun bindGetSavingsGoalByIdUseCase(
        useCaseImpl: GetSavingsGoalByIdUseCaseImpl
    ): GetSavingsGoalByIdUseCase

    @Binds
    @Singleton
    abstract fun bindCreateSavingsGoalUseCase(
        useCaseImpl: CreateSavingsGoalUseCaseImpl
    ): CreateSavingsGoalUseCase

    @Binds
    @Singleton
    abstract fun bindAddFundsToSavingsGoalUseCase(
        useCaseImpl: AddFundsToSavingsGoalUseCaseImpl
    ): AddFundsToSavingsGoalUseCase

    @Binds
    @Singleton
    abstract fun bindSaveSavingsIconUseCase(
        useCaseImpl: SaveSavingsIconUseCaseImpl
    ): SaveSavingsIconUseCase

    @Binds
    @Singleton
    abstract fun bindDeleteSavingsGoalUseCase(
        useCaseImpl: DeleteSavingsGoalUseCaseImpl
    ): DeleteSavingsGoalUseCase


    @Binds
    @ViewModelScoped // Pastikan lifecycle-nya sesuai
    abstract fun bindLoginUserUseCase(
        impl: LoginUserUseCaseImpl
    ): LoginUserUseCase


    @Binds
    @ViewModelScoped
    abstract fun bindRegisterUserUseCase(
        impl: RegisterUserUseCaseImpl
    ): RegisterUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindLogoutUserUseCase(
        impl: LogoutUserUseCaseImpl
    ): LogoutUserUseCase

}