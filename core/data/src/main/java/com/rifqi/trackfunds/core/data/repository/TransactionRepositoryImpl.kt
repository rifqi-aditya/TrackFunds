package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao
) : TransactionRepository {

    override fun getTransactions(): Flow<List<TransactionItem>> {
        return transactionDao.getTransactionsWithDetails().map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override fun getTransactionById(transactionId: String): Flow<TransactionItem?> {
        return getTransactions().map { transactions ->
            transactions.find { it.id == transactionId }
        }
    }

    override fun getTransactionsByType(type: TransactionType): Flow<List<TransactionItem>> {
        return transactionDao.getTransactionsWithDetailsByType(type).map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override fun getTransactionsByDateRange(
        startDate: LocalDate,
        endDate: LocalDate
    ): Flow<List<TransactionItem>> {
        return getTransactions().map { transactions ->
            transactions.filter {
                val transactionDate = it.date.toLocalDate()
                !transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)
            }
        }
    }

    override fun getTransactionsForAccount(accountId: String): Flow<List<TransactionItem>> {
        return getTransactions().map { transactions ->
            transactions.filter { it.accountId == accountId }
        }
    }

    override suspend fun insertTransaction(transaction: TransactionItem) {
        // Konversi ke entity dan masukkan ke database
        transactionDao.insertTransaction(transaction.toEntity())

        // Update saldo akun terkait
        val account = accountDao.getAccountById(transaction.accountId)
        if (account != null) {
            val newBalance = if (transaction.type == TransactionType.INCOME) {
                account.balance.add(transaction.amount)
            } else {
                account.balance.subtract(transaction.amount)
            }
            accountDao.updateAccount(account.copy(balance = newBalance))
        }
    }

    override suspend fun updateTransaction(transaction: TransactionItem) {
        // Ambil transaksi lama untuk menghitung perbedaan saldo
        val oldTransaction = transactionDao.getTransactionById(transaction.id) ?: return

        // Kembalikan saldo dari transaksi lama
        val oldAccount = accountDao.getAccountById(oldTransaction.accountId)
        if (oldAccount != null) {
            val revertedBalance = if (oldTransaction.type == TransactionType.INCOME) {
                oldAccount.balance.subtract(oldTransaction.amount)
            } else {
                oldAccount.balance.add(oldTransaction.amount)
            }
            accountDao.updateAccount(oldAccount.copy(balance = revertedBalance))
        }

        // Terapkan saldo ke akun baru (bisa jadi akun yang sama)
        val newAccount = accountDao.getAccountById(transaction.accountId)
        if (newAccount != null) {
            val newBalance = if (transaction.type == TransactionType.INCOME) {
                newAccount.balance.add(transaction.amount)
            } else {
                newAccount.balance.subtract(transaction.amount)
            }
            accountDao.updateAccount(newAccount.copy(balance = newBalance))
        }

        // Update data transaksi itu sendiri
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(transactionId: String) {
        val transactionToDelete = transactionDao.getTransactionById(transactionId) ?: return

        // Kembalikan saldo dari transaksi yang dihapus
        val account = accountDao.getAccountById(transactionToDelete.accountId)
        if (account != null) {
            val revertedBalance = if (transactionToDelete.type == TransactionType.INCOME) {
                account.balance.subtract(transactionToDelete.amount)
            } else {
                account.balance.add(transactionToDelete.amount)
            }
            accountDao.updateAccount(account.copy(balance = revertedBalance))
        }

        // Hapus transaksi
        transactionDao.deleteTransactionById(transactionId)
    }


    override fun getCategorySummaries(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySummaryItem>> {
        return transactionDao.getCategoryTransactionSummaries(type, startDate, endDate).map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }

    override fun getTransactionsByDateRange(
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>> {
        return transactionDao.getTransactionsWithDetailsByDateRange(startDate, endDate)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }

    override fun getTransactionsByCategoryId(
        categoryId: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>> {
        return transactionDao.getTransactionsWithDetailsByCategoryId(categoryId, startDate, endDate)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }
}