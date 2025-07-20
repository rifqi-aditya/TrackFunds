package com.rifqi.trackfunds.core.domain.di

import com.rifqi.trackfunds.core.domain.usecase.account.AddAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.AddAccountUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.account.DeleteAccountUseCase
import com.rifqi.trackfunds.core.domain.usecase.account.DeleteAccountUseCaseImpl
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
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCase
import com.rifqi.trackfunds.core.domain.usecase.budget.UpdateBudgetUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.AddCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.AddCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.category.UpdateCategoryUseCase
import com.rifqi.trackfunds.core.domain.usecase.category.UpdateCategoryUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.AddFundsToSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.AddFundsToSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.CreateSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.CreateSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.DeleteSavingsGoalUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.DeleteSavingsGoalUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.savings.SaveSavingsIconUseCase
import com.rifqi.trackfunds.core.domain.usecase.savings.SaveSavingsIconUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCase
import com.rifqi.trackfunds.core.domain.usecase.scan.ScanReceiptUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.AddTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.DeleteTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.PerformTransferUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCase
import com.rifqi.trackfunds.core.domain.usecase.transaction.UpdateTransactionUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.user.UpdateUserProfileUseCase
import com.rifqi.trackfunds.core.domain.usecase.user.UpdateUserProfileUseCaseImpl
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
    abstract fun bindLoginUserUseCase(impl: LoginUserUseCaseImpl): LoginUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindRegisterUserUseCase(impl: RegisterUserUseCaseImpl): RegisterUserUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindLogoutUserUseCase(impl: LogoutUserUseCaseImpl): LogoutUserUseCase

    // --- User Use Cases ---
    @Binds
    @ViewModelScoped
    abstract fun bindUpdateUserProfileUseCase(impl: UpdateUserProfileUseCaseImpl): UpdateUserProfileUseCase

    // --- Category Use Cases (Actions) ---
    @Binds
    @ViewModelScoped
    abstract fun bindUpdateCategoryUseCase(impl: UpdateCategoryUseCaseImpl): UpdateCategoryUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindAddCategoryUseCase(impl: AddCategoryUseCaseImpl): AddCategoryUseCase

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

    @Binds
    @ViewModelScoped
    abstract fun bindPerformTransferUseCase(impl: PerformTransferUseCaseImpl): PerformTransferUseCase

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
    abstract fun bindAddFundsToSavingsGoalUseCase(impl: AddFundsToSavingsGoalUseCaseImpl): AddFundsToSavingsGoalUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindSaveSavingsIconUseCase(impl: SaveSavingsIconUseCaseImpl): SaveSavingsIconUseCase

    @Binds
    @ViewModelScoped
    abstract fun bindDeleteSavingsGoalUseCase(impl: DeleteSavingsGoalUseCaseImpl): DeleteSavingsGoalUseCase
}