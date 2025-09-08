package com.rifqi.trackfunds.core.data.repository

import com.rifqi.trackfunds.core.data.local.dao.AccountDao
import com.rifqi.trackfunds.core.data.mapper.toDomain
import com.rifqi.trackfunds.core.data.mapper.toEntity
import com.rifqi.trackfunds.core.domain.account.exception.AccountNotFoundException
import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.auth.exception.NotAuthenticatedException
import com.rifqi.trackfunds.core.domain.auth.repository.UserSessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class AccountRepositoryImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val session: UserSessionRepository // ← ganti ke interface domain
) : AccountRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllAccounts(): Flow<List<Account>> {
        return session.userUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) {
                flowOf(emptyList()) // belum login → kosong (pilihan desain)
            } else {
                accountDao.getAccounts(uid)
                    .map { entities -> entities.map { it.toDomain() } }
            }
        }
        // .distinctUntilChanged() // opsional, jika perlu menahan emisi duplikat
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeAccountCount(): Flow<Int> {
        return session.userUidFlow().flatMapLatest { uid ->
            if (uid.isNullOrBlank()) flowOf(0) else accountDao.observeAccountCount(uid)
        }
    }

    override suspend fun getAccountById(accountId: String): Result<Account> {
        return try {
            val uid = session.requireActiveUserId()
            val entity = accountDao.getAccountById(accountId, uid)
                ?: throw AccountNotFoundException("Account with ID $accountId not found.")
            Result.success(entity.toDomain())
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: NotAuthenticatedException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Catatan: lebih konsisten jika juga mengembalikan Result<List<Account>>,
    // tapi kalau kamu memang ingin "belum login → []", ini OK.
    override suspend fun getAccountsByIds(ids: List<String>): List<Account> {
        return try {
            val uid = session.requireActiveUserId()
            accountDao.getAccountsByIds(ids, uid).map { it.toDomain() }
        } catch (ce: CancellationException) {
            throw ce
        } catch (_: NotAuthenticatedException) {
            emptyList() // pilihan desain: tidak login → list kosong
        }
    }

    override suspend fun saveAccount(account: Account): Result<Unit> {
        return try {
            val uid = session.requireActiveUserId()
            accountDao.upsert(account.toEntity(uid))
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: NotAuthenticatedException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAccount(accountId: String): Result<Unit> {
        return try {
            val uid = session.requireActiveUserId()
            accountDao.deleteAccountById(accountId, uid)
            Result.success(Unit)
        } catch (ce: CancellationException) {
            throw ce
        } catch (e: NotAuthenticatedException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
