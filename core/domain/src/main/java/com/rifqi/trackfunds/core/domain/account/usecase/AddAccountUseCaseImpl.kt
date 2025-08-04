package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.Account
import com.rifqi.trackfunds.core.domain.account.model.AddAccountParams
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class AddAccountUseCaseImpl @Inject constructor(
    private val repository: AccountRepository,
    private val validateAccountUseCase: ValidateAccountUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : AddAccountUseCase {

    override suspend operator fun invoke(params: AddAccountParams): Result<Unit> {
        return runCatching {
            // 1. Validasi input terlebih dahulu
            val validationResult = validateAccountUseCase(
                name = params.name,
                balance = params.initialBalance.toPlainString(),
                icon = params.iconIdentifier,
                isEditMode = false // Selalu false untuk "Add"
            )

            if (!validationResult.isSuccess) {
                // Jika validasi gagal, lemparkan exception dengan pesan error
                val errorMessage = listOfNotNull(
                    validationResult.nameError,
                    validationResult.balanceError,
                    validationResult.iconError
                ).joinToString(separator = "\n")
                throw IllegalArgumentException(errorMessage.ifEmpty { "Invalid input" })
            }

            val userUid = userPreferencesRepository.userUid.first()
                ?: throw IllegalStateException("User not logged in.")

            val newAccount = Account(
                id = UUID.randomUUID().toString(),
                name = params.name,
                balance = params.initialBalance,
                iconIdentifier = params.iconIdentifier
            )

            repository.saveAccount(newAccount, userUid)
        }
    }
}