package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionItemDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.transaction.model.TransactionFilter
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val transactionItemDao: TransactionItemDao,
    private val session: UserSessionRepository
) : TransactionRepository {

    override fun getFilteredTransactions(filter: TransactionFilter): Flow<List<Transaction>> {
        return session.userUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                transactionDao.getFilteredTransactions(
                    userUid = uid,
                    searchQuery = filter.searchQuery.takeIf { it.isNotBlank() }?.let { "%$it%" },
                    type = filter.type?.name,
                    accountIds = filter.accountIds,
                    hasAccountIds = !filter.accountIds.isNullOrEmpty(),
                    categoryIds = filter.categoryIds,
                    hasCategoryIds = !filter.categoryIds.isNullOrEmpty(),
                    startDate = filter.startDate?.atStartOfDay()
                        ?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
                    endDate = filter.endDate?.atTime(23, 59, 59)
                        ?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
                    limit = filter.limit ?: -1
                ).map { dtoList -> dtoList.map { it.toDomain() } }
            }
        }
    }

    override fun getTransactionWithDetails(
        transactionId: String,
        userUid: String
    ): Flow<Transaction?> {
        return transactionDao.getTransactionWithDetails(transactionId, userUid)
            .map { it?.toDomain() }
    }

    override suspend fun findTransactionWithDetailsById(
        transactionId: String,
        userUid: String
    ): Transaction? {
        val dto = transactionDao.findTransactionWithDetailsById(transactionId, userUid)
        return dto?.toDomain()
    }

    override suspend fun saveTransaction(
        transaction: Transaction,
        userUid: String
    ): Result<Unit> = try {
        val txEntity = transaction.toEntity(userUid)
        val itemEntities = transaction.items.map { it.toEntity(transaction.id) }

        transactionDao.upsert(txEntity)
        transactionItemDao.deleteItemsByTransactionId(transaction.id)
        if (itemEntities.isNotEmpty()) transactionItemDao.upsertAll(itemEntities)

        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Method ini TIDAK menerima userUid → ambil dari session.
    override suspend fun deleteTransaction(transactionId: String): Result<Unit> = try {
        val uid = session.requireActiveUserId()
        transactionDao.deleteTransactionById(transactionId, uid)
        Result.success(Unit)
    } catch (ce: CancellationException) {
        throw ce
    } catch (e: NotAuthenticatedException) {
        Result.failure(e)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Sudah menerima userUid → langsung pakai.
    override fun getTransactionsForGoal(
        userUid: String,
        goalId: String
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForGoal(userUid, goalId)
            .map { list -> list.map { it.toDomain() } }
    }
}
