package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.rifqi.trackfunds.core.data.local.entity.TransactionItemEntity

@Dao
interface TransactionItemDao {

    /**
     * Menambah item baru atau memperbarui item yang sudah ada.
     * Digunakan saat menyimpan transaksi beserta detail itemnya.
     */
    @Upsert
    suspend fun upsertAll(items: List<TransactionItemEntity>)

    /**
     * Menghapus semua item yang terkait dengan ID transaksi tertentu.
     * Berguna saat mengedit transaksi, di mana item lama dihapus
     * sebelum item baru dimasukkan.
     */
    @Query("DELETE FROM transaction_items WHERE transaction_id = :transactionId")
    suspend fun deleteItemsByTransactionId(transactionId: String)
}