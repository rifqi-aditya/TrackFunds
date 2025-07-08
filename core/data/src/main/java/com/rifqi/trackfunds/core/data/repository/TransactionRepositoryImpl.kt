package com.rifqi.trackfunds.core.data.repository

import androidx.room.Transaction
import androidx.sqlite.db.SimpleSQLiteQuery
import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.local.dao.SavingsGoalDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val savingsGoalDao: SavingsGoalDao
) : TransactionRepository {

    override fun getFilteredTransactions(filter: TransactionFilter): Flow<List<TransactionItem>> {
        val queryString = StringBuilder()
        val args = mutableListOf<Any?>()

        // Logika SELECT dan JOIN
        queryString.append(
            """
            SELECT
                t.*,
                c.name AS category_name, c.icon_identifier AS category_icon_identifier,
                a.name AS account_name, a.icon_identifier AS account_icon_identifier,
                s.id as savings_goal_id, s.name as savings_goal_name,
                s.icon_identifier as savings_goal_icon_identifier,
                s.target_amount as savings_goal_targetAmount,
                s.current_amount as savings_goal_currentAmount,
                CASE WHEN s.current_amount >= s.target_amount THEN 1 ELSE 0 END as savings_goal_is_achieved
            FROM transactions AS t
            LEFT JOIN categories AS c ON t.category_id = c.id
            INNER JOIN accounts AS a ON t.account_id = a.id
            LEFT JOIN savings_goals AS s ON t.savings_goal_id = s.id
            WHERE 1=1 
            """
        )

        // Logika filter dinamis
        if (filter.searchQuery.isNotBlank()) {
            queryString.append(" AND t.description LIKE ?")
            args.add("%${filter.searchQuery}%")
        }
        filter.type?.let {
            queryString.append(" AND t.type = ?")
            args.add(it.name)
        }
        filter.accountIds?.let {
            if (it.isNotEmpty()) {
                queryString.append(" AND t.account_id IN (${List(it.size) { "?" }.joinToString()})")
                args.addAll(it)
            }
        }
        filter.categoryIds?.let {
            if (it.isNotEmpty()) {
                queryString.append(" AND t.category_id IN (${List(it.size) { "?" }.joinToString()})")
                args.addAll(it)
            }
        }
        if (filter.startDate != null && filter.endDate != null) {
            queryString.append(" AND t.date BETWEEN ? AND ?")
            args.add(filter.startDate!!.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli())
            args.add(filter.endDate!!.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli())
        }

        queryString.append(" ORDER BY t.date DESC")

        filter.limit?.let {
            queryString.append(" LIMIT ?")
            args.add(it)
        }

        return transactionDao.getFilteredTransactionDetailsRaw(
            SimpleSQLiteQuery(
                queryString.toString(),
                args.toTypedArray()
            )
        )
            .map { dtoList -> dtoList.map { it.toDomain() } }
    }

    override fun getTransactionById(transactionId: String): Flow<TransactionItem?> {
        return transactionDao.getTransactionWithDetailsById(transactionId).map { it?.toDomain() }
    }

    @Transaction
    override suspend fun insertTransaction(transaction: TransactionItem) {
        transactionDao.insertTransaction(transaction.toEntity())

        val account = accountDao.getAccountById(transaction.account.id) ?: return
        val newBalance = if (transaction.type == TransactionType.INCOME) {
            account.balance.add(transaction.amount)
        } else {
            account.balance.subtract(transaction.amount)
        }
        accountDao.updateAccount(account.copy(balance = newBalance))

        transaction.savingsGoalItem?.let {
            if (transaction.type == TransactionType.EXPENSE) {
                savingsGoalDao.addFundsToGoal(it.id, transaction.amount)
            }
        }
    }

    @Transaction
    override suspend fun updateTransaction(
        transaction: TransactionItem,
        oldAmount: BigDecimal,
        oldAccountId: String
    ) {
        val newTransactionEntity = transaction.toEntity()

        if (transaction.account.id == oldAccountId) {
            // --- KASUS 1: AKUN TETAP SAMA ---
            val account = accountDao.getAccountById(transaction.account.id) ?: return

            // Hitung selisih perubahan
            val oldEffect =
                if (transaction.type == TransactionType.INCOME) oldAmount else oldAmount.negate()
            val newEffect =
                if (transaction.type == TransactionType.INCOME) transaction.amount else transaction.amount.negate()
            val difference = newEffect - oldEffect

            // Terapkan selisihnya ke saldo saat ini
            val finalBalance = account.balance.add(difference)

            accountDao.updateAccount(account.copy(balance = finalBalance))

        } else {
            // --- KASUS 2: AKUN BERUBAH ---
            // 1. Kembalikan saldo di AKUN LAMA
            val oldAccount = accountDao.getAccountById(oldAccountId)
            if (oldAccount != null) {
                val revertedBalance =
                    if (transaction.type == TransactionType.INCOME) { // Gunakan tipe yang sama karena tidak berubah
                        oldAccount.balance.subtract(oldAmount)
                    } else {
                        oldAccount.balance.add(oldAmount)
                    }
                accountDao.updateAccount(oldAccount.copy(balance = revertedBalance))
            }

            // 2. Terapkan saldo di AKUN BARU
            val newAccount = accountDao.getAccountById(transaction.account.id)
            if (newAccount != null) {
                val newBalance = if (transaction.type == TransactionType.INCOME) {
                    newAccount.balance.add(transaction.amount)
                } else {
                    newAccount.balance.subtract(transaction.amount)
                }
                accountDao.updateAccount(newAccount.copy(balance = newBalance))
            }
        }

        // Terakhir, update data transaksi itu sendiri
        transactionDao.updateTransaction(newTransactionEntity)
    }

    @Transaction
    override suspend fun deleteTransaction(transaction: TransactionItem) {
        transactionDao.deleteTransactionById(transaction.id)

        val account = accountDao.getAccountById(transaction.account.id) ?: return
        val revertedBalance = if (transaction.type == TransactionType.INCOME) {
            account.balance.subtract(transaction.amount)
        } else {
            account.balance.add(transaction.amount)
        }
        accountDao.updateAccount(account.copy(balance = revertedBalance))

        transaction.savingsGoalItem?.let {
            if (transaction.type == TransactionType.EXPENSE) {
                // Kurangi dana dari tujuan tabungan
                savingsGoalDao.addFundsToGoal(it.id, transaction.amount.negate())
            }
        }
    }

    /**
     * FIX: Re-implement the transfer logic.
     */
    @Transaction
    override suspend fun performTransfer(expense: TransactionItem, income: TransactionItem) {
        // 1. Masukkan transaksi pengeluaran
        transactionDao.insertTransaction(expense.toEntity())
        // 2. Masukkan transaksi pemasukan
        transactionDao.insertTransaction(income.toEntity())

        // 3. Kurangi saldo dari akun asal
        val fromAccount = accountDao.getAccountById(expense.account.id)
            ?: throw Exception("Source account for transfer not found")
        val newFromBalance = fromAccount.balance.subtract(expense.amount)
        accountDao.updateAccount(fromAccount.copy(balance = newFromBalance))

        // 4. Tambah saldo ke akun tujuan
        val toAccount = accountDao.getAccountById(income.account.id)
            ?: throw Exception("Destination account for transfer not found")
        val newToBalance = toAccount.balance.add(income.amount)
        accountDao.updateAccount(toAccount.copy(balance = newToBalance))
    }
}