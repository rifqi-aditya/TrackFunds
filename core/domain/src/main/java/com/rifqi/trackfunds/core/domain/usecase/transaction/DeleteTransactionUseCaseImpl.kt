package com.rifqi.trackfunds.core.domain.usecase.transaction

import com.rifqi.trackfunds.core.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCaseImpl @Inject constructor(
    private val transactionRepository: TransactionRepository
) : DeleteTransactionUseCase {

    override suspend operator fun invoke(transactionId: String) {
        // Implementasi di repository akan menangani penghapusan transaksi
        // dan pengembalian saldo akun yang sesuai.
        transactionRepository.deleteTransaction(transactionId)
    }
}