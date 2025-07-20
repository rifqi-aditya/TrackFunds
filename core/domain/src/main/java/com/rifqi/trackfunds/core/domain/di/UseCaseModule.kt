package com.rifqi.trackfunds.core.domain.di

import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsByIdsUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsByIdsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.GetAccountsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetBudgetsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.budget.GetTopBudgetsUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.GetTopBudgetsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesByIdsUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoriesByIdsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryByStandardKeyUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.GetFilteredCategoriesUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetActiveSavingsGoalsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.GetFilteredSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetFilteredSavingsGoalsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.GetSavingsGoalByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.GetSavingsGoalByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetFilteredTransactionsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.GetTransactionByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.user.GetUserProfileUseCase
import com.rifqi.trackfunds.core.domain.usecase.user.GetUserProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    // --- User Use Cases (GET) ---
    @Binds
    @Singleton
    abstract fun bindGetUserProfileUseCase(impl: GetUserProfileUseCaseImpl): GetUserProfileUseCase

    // --- Category Use Cases (GET) ---
    @Binds
    @Singleton
    abstract fun bindGetFilteredCategoriesUseCase(impl: GetFilteredCategoriesUseCaseImpl): GetFilteredCategoriesUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoryUseCase(impl: GetCategoryUseCaseImpl): GetCategoryUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoriesByIdsUseCase(impl: GetCategoriesByIdsUseCaseImpl): GetCategoriesByIdsUseCase

    @Binds
    @Singleton
    abstract fun bindGetCategoryByStandardKeyUseCase(impl: GetCategoryByStandardKeyUseCaseImpl): GetCategoryByStandardKeyUseCase

    // --- Account Use Cases (GET) ---
    @Binds
    @Singleton
    abstract fun bindGetAccountsUseCase(impl: GetAccountsUseCaseImpl): GetAccountsUseCase

    @Binds
    @Singleton
    abstract fun bindGetAccountsByIdsUseCase(impl: GetAccountsByIdsUseCaseImpl): GetAccountsByIdsUseCase

    @Binds
    @Singleton
    abstract fun bindGetAccountByIdUseCase(impl: GetAccountByIdUseCaseImpl): GetAccountByIdUseCase

    // --- Transaction Use Cases (GET) ---
    @Binds
    @Singleton
    abstract fun bindGetFilteredTransactionsUseCase(impl: GetFilteredTransactionsUseCaseImpl): GetFilteredTransactionsUseCase

    @Binds
    @Singleton
    abstract fun bindGetTransactionByIdUseCase(impl: GetTransactionByIdUseCaseImpl): GetTransactionByIdUseCase

    // --- Budget Use Cases (GET) ---
    @Binds
    @Singleton
    abstract fun bindGetBudgetsUseCase(impl: GetBudgetsUseCaseImpl): GetBudgetsUseCase

    @Binds
    @Singleton
    abstract fun bindGetBudgetByIdUseCase(impl: GetBudgetByIdUseCaseImpl): GetBudgetByIdUseCase

    @Binds
    @Singleton
    abstract fun bindGetTopBudgetsUseCase(impl: GetTopBudgetsUseCaseImpl): GetTopBudgetsUseCase

    // --- Savings Use Cases (GET) ---
    @Binds
    @Singleton
    abstract fun bindGetActiveSavingsGoalsUseCase(impl: GetActiveSavingsGoalsUseCaseImpl): GetActiveSavingsGoalsUseCase

    @Binds
    @Singleton
    abstract fun bindGetFilteredSavingsGoalsUseCase(impl: GetFilteredSavingsGoalsUseCaseImpl): GetFilteredSavingsGoalsUseCase

    @Binds
    @Singleton
    abstract fun bindGetSavingsGoalByIdUseCase(impl: GetSavingsGoalByIdUseCaseImpl): GetSavingsGoalByIdUseCase
}