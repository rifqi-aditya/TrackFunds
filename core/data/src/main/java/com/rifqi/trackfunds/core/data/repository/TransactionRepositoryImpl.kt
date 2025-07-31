package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.TransactionDao
import com.rifqi.trackfunds.core.data.local.dao.TransactionItemDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.model.Transaction
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
    private val transactionItemDao: TransactionItemDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : TransactionRepository {

    override fun getFilteredTransactions(
        filter: TransactionFilter,
        userUid: String
    ): Flow<List<Transaction>> = transactionDao.getFilteredTransactions(
        userUid = userUid,
        searchQuery = filter.searchQuery.takeIf { it.isNotBlank() }?.let { "%$it%" },
        type = filter.type?.name,
        accountIds = filter.accountIds,
        hasAccountIds = !filter.accountIds.isNullOrEmpty(),
        categoryIds = filter.categoryIds,
        hasCategoryIds = !filter.categoryIds.isNullOrEmpty(),
        startDate = filter.startDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
        endDate = filter.endDate?.atTime(23, 59, 59)?.toInstant(ZoneOffset.UTC)?.toEpochMilli(),
        limit = filter.limit ?: -1
    ).map { dtoList ->
        dtoList.map { it.toDomain() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTransactionWithDetails(
        transactionId: String,
        userUid: String
    ): Flow<Transaction?> {
        return userPreferencesRepository.userUid.flatMapLatest { userUid ->
            if (userUid == null) {
                flowOf(null)
            } else {
                transactionDao.getTransactionWithDetails(transactionId, userUid)
                    .map { it?.toDomain() }
            }
        }
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
    ): Result<Unit> {
        return try {
            val transactionEntity = transaction.toEntity(userUid)

            val itemEntities = transaction.items.map { it.toEntity(transaction.id) }

            transactionDao.upsert(transactionEntity)

            transactionItemDao.deleteItemsByTransactionId(transaction.id)

            if (itemEntities.isNotEmpty()) {
                transactionItemDao.upsertAll(itemEntities)
            }

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun deleteTransaction(transactionId: String): Result<Unit> {
        return try {
            val userUid = userPreferencesRepository.userUid.first()
                ?: return Result.failure(Exception("User not logged in."))

            transactionDao.deleteTransactionById(transactionId, userUid)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getTransactionsForGoal(userUid: String, goalId: String): Flow<List<Transaction>> {
        return transactionDao.getTransactionsForGoal(userUid, goalId)
            .map { entityList -> entityList.map { it.toDomain() } }
    }
}