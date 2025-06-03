package com.rifqi.trackfunds.core.domain.usecase

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import com.rifqi.trackfunds.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case untuk mendapatkan semua kategori.
 * Mengabstraksi logika pengambilan kategori dari ViewModel.
 */
class GetCategoriesUseCase(
    private val categoryRepository: CategoryRepository
) {
    /**
     * Mengeksekusi use case untuk mendapatkan semua kategori.
     * @return Flow yang berisi daftar semua CategoryItem.
     */
    operator fun invoke(): Flow<List<CategoryItem>> {
        return categoryRepository.getCategories()
    }
}