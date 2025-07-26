package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionModel
import com.rifqi.trackfunds.core.domain.model.TransactionType
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.SavingsRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import javax.inject.Inject

class AddTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val savingsRepository: SavingsRepository,
    private val transactionRunner: AppTransactionRunner
) : AddTransactionUseCase {

    override suspend operator fun invoke(transaction: TransactionModel): Result<Unit> {
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

                transaction.savingsGoalModel?.let {
                    if (transaction.type == TransactionType.EXPENSE) {
                        savingsRepository.addFundsToGoal(it.id, transaction.amount)
                    }
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}