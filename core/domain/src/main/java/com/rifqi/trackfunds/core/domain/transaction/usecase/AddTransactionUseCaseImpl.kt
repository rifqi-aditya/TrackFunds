package com.rifqi.trackfunds.core.domain.transaction.usecase

import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import com.rifqi.trackfunds.core.domain.transaction.model.AddTransactionParams
import com.rifqi.trackfunds.core.domain.transaction.model.Transaction
import com.rifqi.trackfunds.core.domain.transaction.repository.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

class AddTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val transactionRunner: AppTransactionRunner
) : AddTransactionUseCase {

    override suspend operator fun invoke(params: AddTransactionParams): Result<Unit> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(IllegalStateException("User UID is not set."))

        return try {
            transactionRunner {
                val newTransaction = Transaction(
                    id = UUID.randomUUID().toString(),
                    description = params.description,
                    amount = params.amount,
                    type = params.type,
                    date = params.date.atTime(LocalTime.now()),
                    account = params.account,
                    category = params.category,
                    savingsGoal = params.savingsGoal,
                    items = params.items
                )

                val originalAccount =
                    accountRepository.getAccountById(newTransaction.account.id, userUid)
                        .getOrThrow()

                val updatedAccount = originalAccount.applyTransaction(newTransaction)

                transactionRepository.saveTransaction(newTransaction, userUid)

                accountRepository.saveAccount(updatedAccount, userUid)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}