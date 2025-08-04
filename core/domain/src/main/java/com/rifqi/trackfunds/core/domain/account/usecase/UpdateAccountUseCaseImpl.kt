package com.rifqi.trackfunds.core.domain.account.usecase

import com.rifqi.trackfunds.core.domain.account.model.UpdateAccountParams
import com.rifqi.trackfunds.core.domain.account.repository.AccountRepository
import com.rifqi.trackfunds.core.domain.common.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UpdateAccountUseCaseImpl @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
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

            // 2. Dapatkan userUid untuk keamanan
            val userUid = userPreferencesRepository.userUid.first()
                ?: throw IllegalStateException("User not logged in.")

            // 3. Ambil data akun yang asli dari repository
            val originalAccount = accountRepository.getAccountById(params.id, userUid).getOrThrow()

            // 4. Buat objek akun yang sudah diperbarui,
            //    sambil mempertahankan data yang tidak berubah (seperti saldo)
            val updatedAccount = originalAccount.copy(
                name = params.name,
                iconIdentifier = params.iconIdentifier
            )

            // 5. Panggil repository untuk menyimpan perubahan
            accountRepository.saveAccount(updatedAccount, userUid)
        }
    }
}