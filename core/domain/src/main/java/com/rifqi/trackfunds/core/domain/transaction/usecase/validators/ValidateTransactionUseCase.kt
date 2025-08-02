package com.rifqi.trackfunds.core.domain.transaction.usecase.validators

import com.rifqi.trackfunds.core.domain.account.model.Account
import javax.inject.Inject

class ValidateTransactionUseCase @Inject constructor(
    private val validateAmount: ValidateAmount,
    private val validateAccount: ValidateAccount
) {
    operator fun invoke(
        amount: String,
        account: Account?
    ): TransactionValidationResult {
        val amountResult = validateAmount(amount)
        val accountResult = validateAccount(account)

        // Jika ada satu saja yang gagal, langsung kembalikan hasilnya
        if (!amountResult.isSuccess || !accountResult.isSuccess) {
            return TransactionValidationResult(
                amountError = amountResult.errorMessage,
                accountError = accountResult.errorMessage
            )
        }

        // Jika semua berhasil
        return TransactionValidationResult()
    }
}