package com.rifqi.trackfunds.di

import com.rifqi.trackfunds.core.domain.usecase.GetAccountsUseCase
import com.rifqi.trackfunds.core.domain.usecase.GetAccountsUseCaseImpl
import com.rifqi.trackfunds.core.domain.usecase.GetCategoriesUseCase
import com.rifqi.trackfunds.core.domain.usecase.GetCategoriesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class) // Karena Use Case akan di-inject ke ViewModel
abstract class UseCaseModule {

    @Binds
    @ViewModelScoped // Scope agar instance use case hidup selama ViewModel hidup
    abstract fun bindGetCategoriesUseCase(
        useCaseImpl: GetCategoriesUseCaseImpl
    ): GetCategoriesUseCase // Saat ada yang butuh interface GetCategoriesUseCase, berikan implementasinya

    @Binds
    @ViewModelScoped
    abstract fun bindGetAccountsUseCase(
        useCaseImpl: GetAccountsUseCaseImpl
    ): GetAccountsUseCase

    // Tambahkan @Binds untuk use case lain di sini saat Anda membuatnya
}