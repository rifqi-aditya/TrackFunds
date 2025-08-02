package com.rifqi.trackfunds.core.domain.transaction.usecase.validators

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.category.model.Category
import java.time.LocalDate
import javax.inject.Inject

class ValidateTransactionUseCase @Inject constructor(
    private val validateAmount: ValidateAmount,
    private val validateAccount: ValidateAccount,
    private val validateCategory: ValidateCategory,
    private val validateDate: ValidateDate
) {
    operator fun invoke(
        amount: String,
        account: Account?,
        category: Category?,
        date: LocalDate,
    ): TransactionValidationResult {

        val amountResult = validateAmount(amount)
        val accountResult = validateAccount(account)
        val categoryResult = validateCategory(category)
        val dateResult = validateDate(date)

        // Langsung buat hasil akhir, tidak perlu blok if
        return TransactionValidationResult(
            amountError = amountResult.errorMessage,
            accountError = accountResult.errorMessage,
            categoryError = categoryResult.errorMessage,
            dateError = dateResult.errorMessage
        )
    }
}