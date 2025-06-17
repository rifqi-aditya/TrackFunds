package com.rifqi.trackfunds.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.rifqi.trackfunds.core.data.local.dto.CategoryTransactionSummaryDto
import com.rifqi.trackfunds.core.data.local.dto.TransactionDetailDto
import com.rifqi.trackfunds.core.data.local.entity.TransactionEntity
import com.rifqi.trackfunds.core.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TransactionDao {

    /**
     * Query utama untuk mengambil daftar transaksi dengan detail kategori dan akun.
     * Menggunakan LEFT JOIN agar transaksi tetap tampil meskipun kategorinya sudah dihapus.
     */
    @Transaction
    @Query(
        """
        SELECT 
            t.*,
            c.name AS category_name,
            c.icon_identifier AS category_icon_identifier,
            a.name AS account_name
        FROM transactions AS t
        LEFT JOIN categories AS c ON t.category_id = c.id
        INNER JOIN accounts AS a ON t.account_id = a.id
        ORDER BY t.date DESC
    """
    )
    fun getTransactionsWithDetails(): Flow<List<TransactionDetailDto>>

    @Transaction
    @Query(
        """
        SELECT
            t.*,
            c.name AS category_name, 
            c.icon_identifier AS category_icon_identifier,
            a.name AS account_name
        FROM transactions AS t
        INNER JOIN categories AS c ON t.category_id = c.id
        INNER JOIN accounts AS a ON t.account_id = a.id
        WHERE t.id = :transactionId
    """
    )
    fun getTransactionWithDetailsById(transactionId: String): Flow<TransactionDetailDto?>

    @Transaction
    @Query(
        """
        SELECT 
            t.*,
            c.name AS category_name, 
            c.icon_identifier AS category_icon_identifier,
            a.name AS account_name
        FROM transactions AS t
        INNER JOIN categories AS c ON t.category_id = c.id
        INNER JOIN accounts AS a ON t.account_id = a.id
        WHERE t.type = :type
        ORDER BY date DESC
    """
    )
    fun getTransactionsWithDetailsByType(type: TransactionType): Flow<List<TransactionDetailDto>>


    @Transaction
    @Query("""
        SELECT 
            c.id as categoryId, 
            c.name as categoryName, 
            c.icon_identifier as categoryIconIdentifier,
            t.type as transactionType,
            SUM(t.amount) as totalAmount
        FROM transactions AS t
        INNER JOIN categories AS c ON t.category_id = c.id
        WHERE t.type = :type AND t.date BETWEEN :startDate AND :endDate 
        GROUP BY c.id
        ORDER BY totalAmount DESC
    """)
    fun getCategoryTransactionSummaries(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategoryTransactionSummaryDto>>


    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: String): TransactionEntity?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllTransaction(transaction: List<TransactionEntity>)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: String)
}