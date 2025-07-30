package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.SavingsGoalRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AddTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val savingsGoalRepository: SavingsGoalRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val transactionRunner: AppTransactionRunner
) : AddTransactionUseCase {

    override suspend operator fun invoke(transaction: Transaction): Result<Unit> {
        val userUid = userPreferencesRepository.userUidFlow.first()
            ?: return Result.failure(IllegalStateException("User UID is not set."))

        return try {
            transactionRunner {
                // 1. Dapatkan akun yang bersangkutan
                val account = accountRepository.getAccountById(transaction.account.id).getOrThrow()

                // 2. Hitung saldo baru
                val newBalance = if (transaction.type == TransactionType.INCOME) {
                    account.balance.add(transaction.amount)
                } else {
                    account.balance.subtract(transaction.amount)
                }

                // 3. Lakukan semua operasi database
                transactionRepository.insertTransaction(transaction)
                if (transaction.receiptItemModels.isNotEmpty()) {
                    transactionRepository.insertLineItems(
                        transaction.receiptItemModels,
                        transaction.id
                    )
                }
                accountRepository.updateAccount(account.copy(balance = newBalance))

                transaction.savingsGoal?.let {
                    if (transaction.type == TransactionType.SAVINGS) {
                        savingsGoalRepository.addFunds(userUid,it.id, transaction.amount)
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}