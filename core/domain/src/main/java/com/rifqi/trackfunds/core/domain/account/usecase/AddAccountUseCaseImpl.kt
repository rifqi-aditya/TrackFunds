package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.model.AddAccountParams
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import java.util.UUID
import javax.inject.Inject

class AddAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository,
    private val validateAccountUseCase: ValidateAccountUseCase,
) : AddAccountUseCase {

    override suspend operator fun invoke(params: AddAccountParams): Result<Unit> {
        return runCatching {
            val validationResult = validateAccountUseCase(
                name = params.name,
                balance = params.initialBalance.toPlainString(),
                icon = params.iconIdentifier,
                isEditMode = false
            )

            if (!validationResult.isSuccess) {
                val errorMessage = listOfNotNull(
                    validationResult.nameError,
                    validationResult.balanceError,
                    validationResult.iconError
                ).joinToString(separator = "\n")
                throw IllegalArgumentException(errorMessage.ifEmpty { "Invalid input" })
            }

            val newAccount = Account(
                id = UUID.randomUUID().toString(),
                name = params.name,
                balance = params.initialBalance,
                iconIdentifier = params.iconIdentifier
            )

            repository.saveAccount(newAccount)
        }
    }
}