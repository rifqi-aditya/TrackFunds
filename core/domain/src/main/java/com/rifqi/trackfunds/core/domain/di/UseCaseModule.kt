package com.rifqi.trackfunds.core.domain.di

import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountByIdUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsByIdsUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsByIdsUseCaseImpl
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.GetAccountsUseCaseImpl
import com.rifqi.trackfunds.core.domain.account.usecase.ObserveAccountCountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.ObserveAccountCountUseCaseImpl
import com.rifqi.trackfunds.core.domain.budget.usecase.GetBudgetByIdUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.GetBudgetByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.budget.usecase.GetBudgetsUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.GetBudgetsUseCaseImpl
import com.rifqi.trackfunds.core.domain.budget.usecase.GetTopBudgetsUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.GetTopBudgetsUseCaseImpl
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoriesByIdsUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoriesByIdsUseCaseImpl
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoryByStandardKeyUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoryByStandardKeyUseCaseImpl
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoryUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.GetCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.category.usecase.GetFilteredCategoriesUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.GetFilteredCategoriesUseCaseImpl
import com.rifqi.trackfunds.core.domain.reports.usecase.GetCashFlowReportUseCase
import com.rifqi.trackfunds.core.domain.reports.usecase.GetCashFlowReportUseCaseImpl
import com.rifqi.trackfunds.core.domain.reports.usecase.GetExpenseReportUseCase
import com.rifqi.trackfunds.core.domain.reports.usecase.GetExpenseReportUseCaseImpl
import com.rifqi.trackfunds.core.domain.reports.usecase.GetIncomeReportUseCase
import com.rifqi.trackfunds.core.domain.reports.usecase.GetIncomeReportUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.GetActiveSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.GetActiveSavingsGoalsUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.GetFilteredSavingsGoalsUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.GetFilteredSavingsGoalsUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.GetSavingsGoalByIdUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.GetSavingsGoalByIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.GetSavingsGoalDetailsUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.GetSavingsGoalDetailsUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.GetAppVersionUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.GetAppVersionUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.GetThemePreferenceUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.GetThemePreferenceUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.SetThemePreferenceUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.SetThemePreferenceUseCaseImpl
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetFilteredTransactionsUseCaseImpl
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetTransactionDetailsUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetTransactionDetailsUseCaseImpl
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCase
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCaseImpl
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
    abstract fun bindGetUserProfileUseCase(impl: GetUserUseCaseImpl): GetUserUseCase

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
    abstract fun bindObserveAccountCountUseCase(impl: ObserveAccountCountUseCaseImpl): ObserveAccountCountUseCase

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
    abstract fun bindGetTransactionDetailsUseCase(impl: GetTransactionDetailsUseCaseImpl): GetTransactionDetailsUseCase

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
    abstract fun bindGetSavingsGoalDetailsUseCase(impl: GetSavingsGoalDetailsUseCaseImpl): GetSavingsGoalDetailsUseCase

    @Binds
    @Singleton
    abstract fun bindGetSavingsGoalByIdUseCase(impl: GetSavingsGoalByIdUseCaseImpl): GetSavingsGoalByIdUseCase

    @Binds
    abstract fun bindGetThemePreferenceUseCase(
        impl: GetThemePreferenceUseCaseImpl
    ): GetThemePreferenceUseCase

    @Binds
    abstract fun bindSetThemePreferenceUseCase(
        impl: SetThemePreferenceUseCaseImpl
    ): SetThemePreferenceUseCase

    @Binds
    abstract fun bindGetAppVersionUseCase(
        impl: GetAppVersionUseCaseImpl
    ): GetAppVersionUseCase

    @Binds
    abstract fun bindGetCashFlowReportUseCase(
        impl: GetCashFlowReportUseCaseImpl
    ): GetCashFlowReportUseCase

    @Binds
    abstract fun bindGetExpenseReportUseCase(
        impl: GetExpenseReportUseCaseImpl
    ): GetExpenseReportUseCase

    @Binds
    abstract fun bindGetIncomeReportUseCase(
        impl: GetIncomeReportUseCaseImpl
    ): GetIncomeReportUseCase
}