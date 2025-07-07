package com.rifqi.trackfunds.core.data.repository

import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.CategorySummaryItem
import com.rifqi.trackfunds.core.domain.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.time.ZoneOffset
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

    override fun getFilteredTransactions(filter: TransactionFilter): Flow<List<TransactionItem>> {
        val queryString = StringBuilder()
        val args = mutableListOf<Any?>()

        queryString.append(
            """
        SELECT
            t.*,
            c.name AS category_name,
            c.icon_identifier AS category_icon_identifier,
            a.name AS account_name,
            a.icon_identifier AS account_icon_identifier
        FROM transactions AS t
        INNER JOIN categories AS c ON t.category_id = c.id
        INNER JOIN accounts AS a ON t.account_id = a.id
        WHERE 1=1 
        """
        )

        // Filter pencarian (tidak berubah)
        if (filter.searchQuery.isNotBlank()) {
            queryString.append(" AND t.description LIKE ?")
            args.add("%${filter.searchQuery}%")
        }

        // Filter tipe transaksi (tidak berubah)
        val type = filter.type
        if (type != null) {
            queryString.append(" AND t.type = ?")
            args.add(filter.type?.name) // Pastikan dikonversi ke String jika kolomnya TEXT
        }

        // Filter akun (tidak berubah)
        val accountIds = filter.accountIds
        if (!accountIds.isNullOrEmpty()) {
            val placeholders = List(accountIds.size) { "?" }.joinToString(",")
            queryString.append(" AND t.account_id IN ($placeholders)")
            args.addAll(accountIds)
        }

        // Filter kategori (tidak berubah)
        val categoryIds = filter.categoryIds
        if (!categoryIds.isNullOrEmpty()) {
            val placeholders = List(categoryIds.size) { "?" }.joinToString(",")
            queryString.append(" AND t.category_id IN ($placeholders)")
            args.addAll(categoryIds)
        }

        // FIX: Filter tanggal sekarang dinamis
        val startDate = filter.startDate
        val endDate = filter.endDate
        if (startDate != null && endDate != null) {
            queryString.append(" AND t.date BETWEEN ? AND ?")
            args.add(startDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
            args.add(endDate.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli())
        }

        queryString.append(" ORDER BY t.date DESC")

        val simpleSQLiteQuery = SimpleSQLiteQuery(queryString.toString(), args.toTypedArray())

        return transactionDao.getFilteredTransactionDetailsRaw(simpleSQLiteQuery)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }

    override fun getRecentTransactions(limit: Int): Flow<List<TransactionItem>> {
        return transactionDao.getRecentTransactions(limit).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTransactionById(transactionId: String): Flow<TransactionItem?> {
        return transactionDao.getTransactionWithDetailsById(transactionId).map { dto ->
            dto?.toDomain()
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

    @Transaction
    override suspend fun updateTransaction(transaction: TransactionItem) {
        // Ambil transaksi LAMA dari database untuk perbandingan
        val oldTransaction = transactionDao.getTransactionById(transaction.id) ?: return

        // Ambil objek domain dari transaksi BARU (yang dari UI)
        val newTransaction = transaction

        // FIX 2: Bedakan logika jika akunnya sama atau berbeda
        if (oldTransaction.accountId == newTransaction.accountId) {
            // --- KASUS 1: AKUN TETAP SAMA ---
            // Ini adalah kasus paling umum (misal: hanya mengedit catatan/jumlah)

            val account = accountDao.getAccountById(newTransaction.accountId) ?: return

            // Hitung saldo dengan mengembalikan nilai lama, lalu menambah nilai baru
            // Saldo Awal + Pengembalian Nilai Lama - Pengeluaran Nilai Baru
            val balanceAfterRevert = if (oldTransaction.type == TransactionType.INCOME) {
                account.balance.subtract(oldTransaction.amount)
            } else {
                account.balance.add(oldTransaction.amount)
            }

            val finalBalance = if (newTransaction.type == TransactionType.INCOME) {
                balanceAfterRevert.add(newTransaction.amount)
            } else {
                balanceAfterRevert.subtract(newTransaction.amount)
            }

            // Hanya 1x update ke database untuk akun ini
            accountDao.updateAccount(account.copy(balance = finalBalance))

        } else {
            // --- KASUS 2: AKUN BERUBAH ---
            // Logika Anda sebelumnya sudah cukup baik untuk kasus ini

            // 1. Kembalikan saldo di AKUN LAMA
            val oldAccount = accountDao.getAccountById(oldTransaction.accountId)
            if (oldAccount != null) {
                val revertedBalance = if (oldTransaction.type == TransactionType.INCOME) {
                    oldAccount.balance.subtract(oldTransaction.amount)
                } else {
                    oldAccount.balance.add(oldTransaction.amount)
                }
                accountDao.updateAccount(oldAccount.copy(balance = revertedBalance))
            }

            // 2. Terapkan saldo di AKUN BARU
            val newAccount = accountDao.getAccountById(newTransaction.accountId)
            if (newAccount != null) {
                val newBalance = if (newTransaction.type == TransactionType.INCOME) {
                    newAccount.balance.add(newTransaction.amount)
                } else {
                    newAccount.balance.subtract(newTransaction.amount)
                }
                accountDao.updateAccount(newAccount.copy(balance = newBalance))
            }
        }

        // Terakhir, update data transaksi itu sendiri
        transactionDao.updateTransaction(newTransaction.toEntity())
    }

    @Transaction
    override suspend fun deleteTransaction(transactionId: String) {
        // 1. Ambil data transaksi yang akan dihapus untuk tahu jumlah dan tipenya
        val transactionToDelete = transactionDao.getTransactionById(transactionId) ?: return

        // 2. Ambil akun terkait
        val account = accountDao.getAccountById(transactionToDelete.accountId)
        if (account != null) {
            // 3. Hitung saldo yang sudah dikembalikan (reverted)
            val revertedBalance = if (transactionToDelete.type == TransactionType.INCOME) {
                account.balance.subtract(transactionToDelete.amount) // Pemasukan dikurangi
            } else {
                account.balance.add(transactionToDelete.amount) // Pengeluaran ditambahkan kembali
            }
            // 4. Update saldo akun di database
            accountDao.updateAccount(account.copy(balance = revertedBalance))
        }

        // 5. Setelah saldo dikembalikan, baru hapus transaksinya
        transactionDao.deleteTransactionById(transactionId)
    }

    override fun getCategorySummaries(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<CategorySummaryItem>> {
        return transactionDao.getCategoryTransactionSummaries(type, startDate, endDate)
            .map { dtoList ->
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

    override fun getTransactionsByType(
        type: TransactionType,
        startDate: LocalDateTime,
        endDate: LocalDateTime
    ): Flow<List<TransactionItem>> {
        return transactionDao.getTransactionsWithDetailsByType(type, startDate, endDate)
            .map { dtoList ->
                dtoList.map { it.toDomain() }
            }
    }

    @Transaction
    override suspend fun performTransfer(expense: TransactionItem, income: TransactionItem) {
        // 1. Masukkan kedua entitas transaksi
        transactionDao.insertTransaction(expense.toEntity())
        transactionDao.insertTransaction(income.toEntity())

        // 2. Update saldo akun asal
        val fromAccount = accountDao.getAccountById(expense.accountId)
            ?: throw Exception("Source account not found")
        val newFromBalance = fromAccount.balance.subtract(expense.amount)
        accountDao.updateAccount(fromAccount.copy(balance = newFromBalance))

        // 3. Update saldo akun tujuan
        val toAccount = accountDao.getAccountById(income.accountId)
            ?: throw Exception("Destination account not found")
        val newToBalance = toAccount.balance.add(income.amount)
        accountDao.updateAccount(toAccount.copy(balance = newToBalance))
    }

    override fun getExpenseBreakdownForPeriod(startDate: LocalDateTime, endDate: LocalDateTime) =
        transactionDao.getExpenseBreakdown(startDate = startDate, endDate = endDate)
            .map { dtoList -> dtoList.map { it.toDomain() } }

    override fun getIncomeBreakdownForPeriod(startDate: LocalDateTime, endDate: LocalDateTime) =
        transactionDao.getIncomeBreakdown(startDate = startDate, endDate = endDate)
            .map { dtoList -> dtoList.map { it.toDomain() } }

    override fun getCashFlowSummaryForPeriod(startDate: LocalDateTime, endDate: LocalDateTime) =
        transactionDao.getCashFlowSummary(startDate = startDate, endDate = endDate)
            .map { it.toDomain() }


    override fun getTransactionsByGoalId(goalId: String): Flow<List<TransactionItem>> {
        return transactionDao.getTransactionsByGoalId(goalId).map { dtoList ->
            dtoList.map { it.toDomain() }
        }
    }
}