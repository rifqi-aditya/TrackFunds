package com.rifqi.trackfunds.core.domain.usecase.category

import com.rifqi.trackfunds.core.domain.model.CategoryItem

/**
 * Use case untuk mencari satu kategori berdasarkan kunci standarnya.
 */
interface GetCategoryByStandardKeyUseCase {
    /**
     * @param key Kunci standar yang ingin dicari (contoh: "transportation").
     * @return Objek CategoryItem jika ditemukan, atau null jika tidak ada.
     */
    suspend operator fun invoke(key: String): CategoryItem?
}