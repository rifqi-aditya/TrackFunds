package com.rifqi.trackfunds.core.domain.di

import com.rifqi.trackfunds.core.domain.account.usecase.AddAccountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.AddAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.account.usecase.DeleteAccountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.DeleteAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.account.usecase.UpdateAccountUseCase
import com.rifqi.trackfunds.core.domain.account.usecase.UpdateAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.auth.usecase.LoginUseCase
import com.rifqi.trackfunds.core.domain.auth.usecase.LoginUseCaseImpl
import com.rifqi.trackfunds.core.domain.auth.usecase.LogoutUseCase
import com.rifqi.trackfunds.core.domain.auth.usecase.LogoutUseCaseImpl
import com.rifqi.trackfunds.core.domain.auth.usecase.RegisterUseCase
import com.rifqi.trackfunds.core.domain.auth.usecase.RegisterUseCaseImpl
import com.rifqi.trackfunds.core.domain.budget.usecase.AddBudgetUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.AddBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.budget.usecase.CheckExistingBudgetUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.CheckExistingBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.budget.usecase.DeleteBudgetUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.DeleteBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.budget.usecase.UpdateBudgetUseCase
import com.rifqi.trackfunds.core.domain.budget.usecase.UpdateBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.category.usecase.AddCategoryUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.AddCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.category.usecase.DeleteCategoryUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.DeleteCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.category.usecase.UpdateCategoryUseCase
import com.rifqi.trackfunds.core.domain.category.usecase.UpdateCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.AddFundsToSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.AddFundsToSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.CreateSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.CreateSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.DeleteSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.DeleteSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.savings.usecase.UpdateSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.savings.usecase.UpdateSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.scan.usecase.ScanReceiptUseCase
import com.rifqi.trackfunds.core.domain.scan.usecase.ScanReceiptUseCaseImpl
import com.rifqi.trackfunds.core.domain.transaction.usecase.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.AddTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.transaction.usecase.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.DeleteTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.transaction.usecase.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.domain.transaction.usecase.UpdateTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.user.usecase.UpdateProfileUseCase
import com.rifqi.trackfunds.core.domain.user.usecase.UpdateProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelScopedUseCaseModule {

    // --- Auth Use Cases ---
    @Binds
    @ViewModelScoped
    abstract fun bindLoginUserUseCase(impl: LoginUseCaseImpl): LoginUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindRegisterUserUseCase(impl: RegisterUseCaseImpl): RegisterUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindLogoutUserUseCase(impl: LogoutUseCaseImpl): LogoutUseCase

    // --- User Use Cases ---
    @Binds
    @ViewModelScoped
    abstract fun bindUpdateUserProfileUseCase(impl: UpdateProfileUseCaseImpl): UpdateProfileUseCase

    // --- Category Use Cases (Actions) ---
    @Binds
    @ViewModelScoped
    abstract fun bindUpdateCategoryUseCase(impl: UpdateCategoryUseCaseImpl): UpdateCategoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindAddCategoryUseCase(impl: AddCategoryUseCaseImpl): AddCategoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteCategoryUseCase(impl: DeleteCategoryUseCaseImpl): DeleteCategoryUseCase

    // --- Account Use Cases (Actions) ---
    @Binds
    @ViewModelScoped
    abstract fun bindAddAccountUseCase(impl: AddAccountUseCaseImpl): AddAccountUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateAccountUseCase(impl: UpdateAccountUseCaseImpl): UpdateAccountUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteAccountUseCase(impl: DeleteAccountUseCaseImpl): DeleteAccountUseCase

    // --- Transaction Use Cases (Actions) ---
    @Binds
    @ViewModelScoped
    abstract fun bindAddTransactionUseCase(impl: AddTransactionUseCaseImpl): AddTransactionUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateTransactionUseCase(impl: UpdateTransactionUseCaseImpl): UpdateTransactionUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteTransactionUseCase(impl: DeleteTransactionUseCaseImpl): DeleteTransactionUseCase

    // --- Budget Use Cases (Actions) ---
    @Binds
    @ViewModelScoped
    abstract fun bindAddBudgetUseCase(impl: AddBudgetUseCaseImpl): AddBudgetUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateBudgetUseCase(impl: UpdateBudgetUseCaseImpl): UpdateBudgetUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteBudgetUseCase(impl: DeleteBudgetUseCaseImpl): DeleteBudgetUseCase

    @Binds
    @ViewModelScoped
    abstract fun bingCheckExistingBudgetUseCase(impl: CheckExistingBudgetUseCaseImpl): CheckExistingBudgetUseCase

    // --- Scan Use Case ---
    @Binds
    @ViewModelScoped
    abstract fun bindScanReceiptUseCase(impl: ScanReceiptUseCaseImpl): ScanReceiptUseCase

    // --- Savings Use Cases (Actions) ---
    @Binds
    @ViewModelScoped
    abstract fun bindCreateSavingsGoalUseCase(impl: CreateSavingsGoalUseCaseImpl): CreateSavingsGoalUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindUpdateSavingsGoalUseCase(impl: UpdateSavingsGoalUseCaseImpl): UpdateSavingsGoalUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindAddFundsToSavingsGoalUseCase(impl: AddFundsToSavingsGoalUseCaseImpl): AddFundsToSavingsGoalUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteSavingsGoalUseCase(impl: DeleteSavingsGoalUseCaseImpl): DeleteSavingsGoalUseCase
}