package com.rifqi.trackfunds.core.domain.repository

import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Interface (kontrak) untuk Repository Transaksi.
 */
interface TransactionRepository {

    // --- READ ---
    /**
     * Mengambil semua transaksi sebagai stream data yang reaktif.
     */
    fun getTransactions(): Flow<List<TransactionItem>>

    /**
     * Mengambil transaksi dalam rentang tanggal tertentu.
     */
    fun getTransactionsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<TransactionItem>>

    /**
     * Mengambil transaksi berdasarkan tipe (EXPENSE atau INCOME).
     */
    fun getTransactionsByType(type: TransactionType): Flow<List<TransactionItem>>

    /**
     * Mengambil transaksi untuk akun spesifik.
     */
    fun getTransactionsForAccount(accountId: String): Flow<List<TransactionItem>>

    /**
     * Mengambil satu transaksi berdasarkan ID-nya.
     */
    fun getTransactionById(transactionId: String): Flow<TransactionItem?>

    // --- CREATE ---
    /**
     * Menyimpan transaksi baru ke dalam data source.
     * Ini juga harus mengupdate saldo akun terkait.
     */
    suspend fun insertTransaction(transaction: TransactionItem)

    // --- UPDATE ---
    /**
     * Memperbarui transaksi yang sudah ada.
     * Ini harus menangani perubahan pada saldo akun yang lama dan baru jika akun berubah.
     */
    suspend fun updateTransaction(transaction: TransactionItem)

    // --- DELETE ---
    /**
     * Menghapus transaksi berdasarkan ID.
     * Ini juga harus mengembalikan (revert) saldo akun terkait.
     */
    suspend fun deleteTransaction(transactionId: String)

    fun getCategorySummaries(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySummaryItem>>
}