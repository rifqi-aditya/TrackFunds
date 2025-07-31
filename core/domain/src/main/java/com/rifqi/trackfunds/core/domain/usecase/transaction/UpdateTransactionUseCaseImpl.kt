package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.Transaction
import com.rifqi.trackfunds.core.domain.model.params.UpdateTransactionParams
import com.rifqi.trackfunds.core.domain.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import com.rifqi.trackfunds.core.domain.repository.UserPreferencesRepository
import com.rifqi.trackfunds.core.domain.transaction.AppTransactionRunner
import kotlinx.coroutines.flow.first
import java.time.LocalTime
import javax.inject.Inject

class UpdateTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val accountRepository: AccountRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val transactionRunner: AppTransactionRunner
) : UpdateTransactionUseCase {

    override suspend operator fun invoke(params: UpdateTransactionParams): Result<Unit> {
        val userUid = userPreferencesRepository.userUid.first()
            ?: return Result.failure(IllegalStateException("User UID is not set."))

        return try {
            transactionRunner {
                val originalTransaction = transactionRepository.findTransactionWithDetailsById(
                    params.id,
                    userUid
                ) ?: throw IllegalStateException("Transaksi original tidak ditemukan.")

                val updatedTransaction = Transaction(
                    id = originalTransaction.id,
                    description = params.description,
                    amount = params.amount,
                    type = params.type,
                    date = params.date.atTime(LocalTime.now()),
                    account = params.account,
                    category = params.category ?: originalTransaction.category,
                    savingsGoal = params.savingsGoal ?: originalTransaction.savingsGoal,
                    items = params.items
                )

                // 2. Batalkan efek transaksi LAMA dari akun LAMA
                val oldAccount =
                    accountRepository.getAccountById(originalTransaction.account.id).getOrThrow()
                val revertedAccount = oldAccount.revertTransaction(originalTransaction)
                accountRepository.updateAccount(revertedAccount)

                // 3. Terapkan efek transaksi BARU ke akun TUJUAN
                val targetAccount =
                    if (updatedTransaction.account.id == originalTransaction.account.id) {
                        revertedAccount
                    } else {
                        accountRepository.getAccountById(updatedTransaction.account.id).getOrThrow()
                    }

                val finalAccount = targetAccount.applyTransaction(updatedTransaction)

                accountRepository.updateAccount(finalAccount)

                // 4. Simpan perubahan transaksi ke database
                transactionRepository.saveTransaction(updatedTransaction, userUid)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}