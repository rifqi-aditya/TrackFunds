package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.CategoryItem
import kotlinx.coroutines.flow.Flow

/**
 * Interface yang mendefinisikan kontrak untuk operasi terkait data Kategori.
 * Lapisan domain akan menggunakan interface ini, dan lapisan data akan mengimplementasikannya.
 */
interface CategoryRepository {

    /**
     * Mengambil semua kategori sebagai stream data (Flow).
     * Flow akan emit list baru setiap kali ada perubahan pada data kategori.
     * @return Flow yang berisi daftar semua CategoryItem.
     */
    fun getCategories(): Flow<List<CategoryItem>>

    /**
     * (Contoh fungsi untuk masa depan)
     * Mengambil kategori berdasarkan tipenya.
     * @param type Tipe transaksi (EXPENSE, INCOME, DEBT_LOAN).
     * @return Flow yang berisi daftar CategoryItem yang sesuai dengan tipe.
     */
    // fun getCategoriesByType(type: TransactionType): Flow<List<CategoryItem>>

    /**
     * (Contoh fungsi untuk masa depan)
     * Menambahkan kategori baru.
     * @param category CategoryItem yang akan ditambahkan.
     */
    // suspend fun addCategory(category: CategoryItem)

    /**
     * (Contoh fungsi untuk masa depan)
     * Mendapatkan kategori spesifik berdasarkan ID-nya.
     * @param id ID dari kategori yang dicari.
     * @return Flow yang berisi CategoryItem atau null jika tidak ditemukan.
     */
//     suspend fun getCategoryById(id: String): Flow<CategoryItem?>

    // Anda bisa menambahkan fungsi-fungsi lain di sini sesuai kebutuhan aplikasi,
    // seperti updateCategory(category: CategoryItem), deleteCategory(id: String), dll.
}