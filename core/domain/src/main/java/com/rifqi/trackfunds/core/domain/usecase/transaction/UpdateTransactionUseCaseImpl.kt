package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.model.TransactionItem
import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : UpdateTransactionUseCase {

    override suspend operator fun invoke(transaction: TransactionItem) {
        // Nantinya, implementasi di repository akan menangani kalkulasi ulang saldo
        // jika jumlah atau akun dari transaksi berubah.
        transactionRepository.updateTransaction(transaction)
    }
}