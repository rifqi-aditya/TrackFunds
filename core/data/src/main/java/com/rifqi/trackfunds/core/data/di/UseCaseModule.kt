package com.rifqi.trackfunds.core.data.di

import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import com.rifqi.trackfunds.core.domain.usecase.GetCategoriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // Install di SingletonComponent karena use case seringkali stateless dan bisa singleton
object UseCaseModule { // Gunakan 'object' karena semua methodnya @Provides (stateless)

    @Provides
    // @Singleton // Anda bisa menambahkan ini jika GetCategoriesUseCase stateless dan ingin Singleton
    fun provideGetCategoriesUseCase(
        categoryRepository: CategoryRepository // Hilt akan menyediakan ini dari RepositoryModule Anda
    ): GetCategoriesUseCase {
        // Hilt akan memanggil konstruktor GetCategoriesUseCase dan menyediakan dependensinya
        return GetCategoriesUseCase(categoryRepository)
    }

    // Anda bisa menambahkan @Provides method untuk use case lain di sini
}