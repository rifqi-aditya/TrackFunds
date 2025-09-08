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
import com.rifqi.trackfunds.core.domain.settings.usecase.GetAppVersionUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.GetAppVersionUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveActiveUserIdUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveActiveUserIdUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveAppThemeUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveAppThemeUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveLocaleUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.ObserveLocaleUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.SetAppThemeUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.SetAppThemeUseCaseImpl
import com.rifqi.trackfunds.core.domain.settings.usecase.SetLocaleUseCase
import com.rifqi.trackfunds.core.domain.settings.usecase.SetLocaleUseCaseImpl
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetFilteredTransactionsUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetFilteredTransactionsUseCaseImpl
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetTransactionDetailsUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.GetTransactionDetailsUseCaseImpl
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCase
import com.rifqi.trackfunds.core.domain.user.usecase.GetUserUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    // --- User (GET) ---
    @Binds
    @Reusable
    abstract fun bindGetUserProfileUseCase(impl: GetUserUseCaseImpl): GetUserUseCase

    // --- Category (GET) ---
    @Binds
    @Reusable
    abstract fun bindGetFilteredCategoriesUseCase(impl: GetFilteredCategoriesUseCaseImpl): GetFilteredCategoriesUseCase

    @Binds
    @Reusable
    abstract fun bindGetCategoryUseCase(impl: GetCategoryUseCaseImpl): GetCategoryUseCase

    @Binds
    @Reusable
    abstract fun bindGetCategoriesByIdsUseCase(impl: GetCategoriesByIdsUseCaseImpl): GetCategoriesByIdsUseCase

    @Binds
    @Reusable
    abstract fun bindGetCategoryByStandardKeyUseCase(impl: GetCategoryByStandardKeyUseCaseImpl): GetCategoryByStandardKeyUseCase

    // --- Account (GET) ---
    @Binds
    @Reusable
    abstract fun bindGetAccountsUseCase(impl: GetAccountsUseCaseImpl): GetAccountsUseCase

    @Binds
    @Reusable
    abstract fun bindObserveAccountCountUseCase(impl: ObserveAccountCountUseCaseImpl): ObserveAccountCountUseCase

    @Binds
    @Reusable
    abstract fun bindGetAccountsByIdsUseCase(impl: GetAccountsByIdsUseCaseImpl): GetAccountsByIdsUseCase

    @Binds
    @Reusable
    abstract fun bindGetAccountByIdUseCase(impl: GetAccountByIdUseCaseImpl): GetAccountByIdUseCase

    // --- Transaction (GET) ---
    @Binds
    @Reusable
    abstract fun bindGetFilteredTransactionsUseCase(impl: GetFilteredTransactionsUseCaseImpl): GetFilteredTransactionsUseCase

    @Binds
    @Reusable
    abstract fun bindGetTransactionDetailsUseCase(impl: GetTransactionDetailsUseCaseImpl): GetTransactionDetailsUseCase

    // --- Budget (GET) ---
    @Binds
    @Reusable
    abstract fun bindGetBudgetsUseCase(impl: GetBudgetsUseCaseImpl): GetBudgetsUseCase

    @Binds
    @Reusable
    abstract fun bindGetBudgetByIdUseCase(impl: GetBudgetByIdUseCaseImpl): GetBudgetByIdUseCase

    @Binds
    @Reusable
    abstract fun bindGetTopBudgetsUseCase(impl: GetTopBudgetsUseCaseImpl): GetTopBudgetsUseCase




    // --- App prefs (Theme & Locale) ---
    @Binds
    @Reusable
    abstract fun bindObserveAppThemeUseCase(impl: ObserveAppThemeUseCaseImpl): ObserveAppThemeUseCase

    @Binds
    @Reusable
    abstract fun bindSetAppThemeUseCase(impl: SetAppThemeUseCaseImpl): SetAppThemeUseCase

    @Binds
    @Reusable
    abstract fun bindObserveLocaleUseCase(impl: ObserveLocaleUseCaseImpl): ObserveLocaleUseCase

    @Binds
    @Reusable
    abstract fun bindSetLocaleUseCase(impl: SetLocaleUseCaseImpl): SetLocaleUseCase

    @Binds
    @Reusable
    abstract fun bindObserveActiveUserIdUseCase(impl: ObserveActiveUserIdUseCaseImpl): ObserveActiveUserIdUseCase

    @Binds
    @Reusable
    abstract fun bindGetAppVersionUseCase(
        impl: GetAppVersionUseCaseImpl
    ): GetAppVersionUseCase

    // --- Reports (GET) ---
    @Binds
    @Reusable
    abstract fun bindGetCashFlowReportUseCase(impl: GetCashFlowReportUseCaseImpl): GetCashFlowReportUseCase

    @Binds
    @Reusable
    abstract fun bindGetExpenseReportUseCase(impl: GetExpenseReportUseCaseImpl): GetExpenseReportUseCase

    @Binds
    @Reusable
    abstract fun bindGetIncomeReportUseCase(impl: GetIncomeReportUseCaseImpl): GetIncomeReportUseCase
}
