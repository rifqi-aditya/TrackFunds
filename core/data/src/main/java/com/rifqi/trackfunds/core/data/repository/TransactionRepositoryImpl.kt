package com.rifqi.trackfunds.core.data.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.model.filter.TransactionFilter
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : TransactionRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getFilteredTransactions(filter: TransactionFilter): Flow<List<TransactionItem>> {
        return userPreferencesRepository.userUidFlow.flatMapLatest { userUid ->
            if (userUid == null) return@flatMapLatest flowOf(emptyList())

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
            WHERE t.user_uid = ? 
            """
            )

            args.add(userUid)

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
                args.add(
                    filter.endDate!!.atTime(23, 59, 59).toInstant(ZoneOffset.UTC).toEpochMilli()
                )
            }

            queryString.append(" ORDER BY t.date DESC")

            filter.limit?.let {
                queryString.append(" LIMIT ?")
                args.add(it)
            }

            transactionDao.getFilteredTransactionDetailsRaw(
                SimpleSQLiteQuery(queryString.toString(), args.toTypedArray())
            ).map { dtoList -> dtoList.map { it.toDomain() } }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTransactionById(transactionId: String): Flow<TransactionItem?> {
        return userPreferencesRepository.userUidFlow.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(null)
            } else {
                transactionDao.getTransactionWithDetailsById(transactionId, userUid)
                    .map { it?.toDomain() }
            }
        }
    }

    override suspend fun insertTransaction(transaction: TransactionItem): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))
            transactionDao.insertTransaction(transaction.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTransaction(transaction: TransactionItem): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))
            transactionDao.updateTransaction(transaction.toEntity(userUid))
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTransaction(transactionId: String): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUidFlow.first()
                ?: return Result.failure(Exception("User not logged in."))

            transactionDao.deleteTransactionById(transactionId, userUid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}