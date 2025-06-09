package com.rifqi.trackfunds.core.data.di

import com.rifqi.trackfunds.core.data.repository.AccountRepositoryImpl
import com.rifqi.trackfunds.core.data.repository.CategoryRepositoryImpl
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl // Hilt tahu cara membuat ini karena constructornya @Inject
    ): CategoryRepository // Saat CategoryRepository diminta, Hilt akan menyediakan CategoryRepositoryImpl

    @Binds
    abstract fun bindAccountRepository(
        accountRepositoryImpl: AccountRepositoryImpl
    ): AccountRepository
}