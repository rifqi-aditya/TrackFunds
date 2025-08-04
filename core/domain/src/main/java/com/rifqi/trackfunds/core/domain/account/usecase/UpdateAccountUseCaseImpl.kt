package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.UpdateAccountParams
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val validateAccountUseCase: ValidateAccountUseCase
) : UpdateAccountUseCase {

    override suspend operator fun invoke(params: UpdateAccountParams): Result<Unit> {
        return runCatching {
            val validationResult = validateAccountUseCase(
                name = params.name,
                icon = params.iconIdentifier,
                balance = "",
                isEditMode = true
            )

            if (!validationResult.isSuccess) {
                val errorMessage = validationResult.nameError ?: validationResult.iconError
                throw IllegalArgumentException(errorMessage ?: "Invalid input")
            }

            val originalAccount = accountRepository.getAccountById(params.id).getOrThrow()

            val updatedAccount = originalAccount.copy(
                name = params.name,
                iconIdentifier = params.iconIdentifier
            )

            accountRepository.saveAccount(updatedAccount)
        }
    }
}